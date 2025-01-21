package crafter_coder.global.scheduler;

import crafter_coder.domain.payment.dto.PaymentReqDto;
import crafter_coder.domain.program.model.Program;
import crafter_coder.domain.program.model.ProgramStatus;
import crafter_coder.domain.program.repository.ProgramRepository;
import crafter_coder.domain.subscription.model.Subscription;
import crafter_coder.global.openFeign.client.PaymentApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentScheduler {

    private final ProgramRepository programRepository;
    private final PaymentApiClient paymentApiClient;
    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void requestSubscriptionPayment() {
        log.info("00:00:00 정기 결제 구독 결제 확인 시작");

        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();

        if(dayOfMonth > 28) {
            log.info("결제 가능 일이 아니므로 스케줄러를 종료합니다.");
        } else {
            processProgramPayments(dayOfMonth);
        }
    }

    private void processProgramPayments(int dayOfMonth) {
        // ACTIVE 상태인 프로그램들 중 결제일이 오늘인 프로그램들을 조회
        List<Program> programs = programRepository.findByBillingDateAndStatusWithSubscriptions(dayOfMonth, ProgramStatus.ACTIVE);
        for (Program program : programs) {
            String depositAccountNumber = program.getAccountNumber();
            BigDecimal amount = program.getPrice();
            // ACTIVE 상태인 구독들에 대해 결제 요청해야 함
            List<Subscription> subscriptions = program.getSubscriptions().stream().filter(Subscription::isActive).toList();
            for (Subscription subscription : subscriptions) {
                String withdrawAccountNumber = subscription.getAccountNumber();
                String password = subscription.getAccountPassword();
                paymentApiClient.requestPayment(PaymentReqDto.of(withdrawAccountNumber, depositAccountNumber, amount, password));
                // TODO: 잔액 부족으로 결제 실패 시 해당 계좌 구독 상태 INACTIVE로 변경(try-catch)
            }
        }
    }
}
