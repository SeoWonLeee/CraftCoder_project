package crafter_coder.domain.notification.event;

import crafter_coder.domain.notification.NotificationDto;
import crafter_coder.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;

    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        NotificationDto notificationDto = event.getNotificationDto();
        notificationService.send(notificationDto.receiverId(), notificationDto.notificationType(), notificationDto.content());
    }
}
