package crafter_coder.domain.notification.event;

import crafter_coder.domain.notification.NotificationDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationEvent extends ApplicationEvent {
    private final NotificationDto notificationDto;

    private NotificationEvent(Object source, NotificationDto notificationDto) {
        super(source);
        this.notificationDto = notificationDto;
    }

    public static NotificationEvent of(Object source, NotificationDto notificationDto) {
        return new NotificationEvent(source, notificationDto);
    }
}
