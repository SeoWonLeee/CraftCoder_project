package crafter_coder.global.scheduler;

import crafter_coder.domain.payment.dto.PaymentReqDto;
import crafter_coder.domain.program.model.Program;
import crafter_coder.domain.program.model.ProgramStatus;
import crafter_coder.domain.program.repository.ProgramRepository;
import crafter_coder.domain.subscription.model.Subscription;
import crafter_coder.global.openFeign.client.PaymentApiClient;
import crafter_coder.global.util.AesUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentScheduler {
    private final PaymentRequestFacade paymentRequestFacade;
    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void requestSubscriptionPayment() {
        log.info("00:00:00 정기 결제 구독 결제 확인 시작");

        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();

        if(dayOfMonth > 28) {
            log.info("결제 가능 일이 아니므로 스케줄러를 종료합니다.");
        } else {
            log.info("결제 가능 일이므로 결제를 진행합니다.");
            paymentRequestFacade.processProgramPayments(dayOfMonth);
        }
    }



    //

}
