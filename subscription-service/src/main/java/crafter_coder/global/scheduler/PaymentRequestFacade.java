package crafter_coder.global.scheduler;

import crafter_coder.domain.payment.dto.PaymentReqDto;
import crafter_coder.domain.payment.dto.PaymentResDto;
import crafter_coder.domain.payment.model.Payment;
import crafter_coder.domain.payment.model.PaymentStatus;
import crafter_coder.domain.payment.repository.PaymentRepository;
import crafter_coder.domain.program.model.Program;
import crafter_coder.domain.program.model.ProgramStatus;
import crafter_coder.domain.program.repository.ProgramRepository;
import crafter_coder.domain.subscription.model.Subscription;
import crafter_coder.domain.subscription.model.SubscriptionStatus;
import crafter_coder.global.openFeign.client.PaymentApiClient;
import crafter_coder.global.openFeign.exception.RestApiException;
import crafter_coder.global.util.AesUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentRequestFacade {
    private final ProgramRepository programRepository;
    private final PaymentApiClient paymentApiClient;
    private final PaymentRepository paymentRepository;
    private final AesUtil aesUtil;

    @Transactional
    public void processProgramPayments(int dayOfMonth) {
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

                // request body를 암호화해서 결제 요청
                try {
                    PaymentResDto response = paymentApiClient.requestPayment(
                            PaymentReqDto.of(
                                    aesUtil.encryptData(withdrawAccountNumber),
                                    aesUtil.encryptData(depositAccountNumber),
                                    amount,
                                    aesUtil.encryptData(password)
                            ));

                    Payment payment = Payment.of(response.paymentId(), amount, PaymentStatus.DONE, subscription);
                    paymentRepository.save(payment);
                } catch (RestApiException e) { // 잔액 부족으로 결제 실패 시 해당 계좌 구독 상태 INACTIVE로 변경
                    log.error("결제 실패, subscriptionId={}", subscription.getId());
                    String pattern = ".*(잔액 부족|존재하지 않는 계좌|인증 실패).*";
                    if (e.getMessage().matches(pattern)) {
                        log.info("구독 상태를 INACTIVE로 변경합니다. subscriptionId={}", subscription.getId());
                        subscription.updateStatus(SubscriptionStatus.INACTIVE);
                    }
                }
            }
        }
    }
}
