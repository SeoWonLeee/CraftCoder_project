package crafter_coder.domain.notification.event;

import crafter_coder.domain.notification.NotificationDto;
import crafter_coder.domain.notification.NotificationType;
import crafter_coder.domain.notification.emitter.EmitterRepository;
import crafter_coder.domain.notification.service.NotificationService;
import crafter_coder.domain.notification.util.IdTimestampUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static crafter_coder.domain.notification.NotificationType.*;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;
    private final EmitterRepository emitterRepository;
    private final IdTimestampUtil idTimestampUtil;

    @Async // 별도의 스레드에서 처리
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true) // 트랜잭션 커밋 시에만 실행, 혹은 트랜잭션이 아예 없어도 실행
    public void handleNotificationEvent(NotificationEvent event) {
        NotificationDto notificationDto = event.getNotificationDto();
        NotificationType notificationType = notificationDto.notificationType();

        if(notificationType.equals(COURSE_OPEN)) { // COURSE_OPEN은 모든 사용자에게 알림
            emitterRepository.getAllEmitters().forEach((key, value) -> {
                Long userId = idTimestampUtil.extractUserId(key);
                String eventId = idTimestampUtil.makeTimeIncludeId(userId);
                notificationService.send(eventId, notificationType, notificationDto.content());
            });
            return;
        } else if(notificationType.equals(COURSE_CANCELLATION)) { // COURSE_CANCELLATION은 해당 강좌를 수강했던 사용자에게 알림
            // TODO: userCourse에서 해당 강좌를 수강한 사용자들을 찾아서 알림을 보내야 함
        } else if(notificationType.equals(AVAILABLE_SEAT)) { // COURSE_UPDATE은 대기열 첫 순번 사용자에게 알림
            String eventId = idTimestampUtil.makeTimeIncludeId(notificationDto.receiverId());
            notificationService.send(eventId, notificationDto.notificationType(), notificationDto.content());
        }
    }
}
