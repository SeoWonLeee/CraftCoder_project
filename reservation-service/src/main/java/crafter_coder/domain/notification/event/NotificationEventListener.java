package crafter_coder.domain.notification.event;

import crafter_coder.domain.notification.service.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationSender notificationSender;

    @Async // 별도의 스레드에서 처리
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true) // 트랜잭션 커밋 시에만 실행, 혹은 트랜잭션이 아예 없어도 실행
    public void handleNotificationEvent(NotificationEvent event) {
        notificationSender.sendNotification(event);
    }

}
