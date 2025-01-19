package crafter_coder.global.scheduler;

import org.springframework.scheduling.annotation.Scheduled;

public class PaymentScheduler {
    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void requestSubscriptionPayment() {

    }
}
