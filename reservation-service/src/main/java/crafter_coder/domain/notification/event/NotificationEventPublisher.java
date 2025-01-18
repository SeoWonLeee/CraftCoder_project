package crafter_coder.domain.notification.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class NotificationEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void publish(NotificationEvent event) {
        eventPublisher.publishEvent(event);
    }
}
