package crafter_coder.global.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentScheduler {
    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void requestSubscriptionPayment() {

    }
}
