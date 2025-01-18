package crafter_coder.domain.notification.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class NotificationExceptionHandler {

    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<NotificationErrorCode> handleNotificationException(NotificationException e) {
        log.error("NotificationException 발생: {}", e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode());
    }
}
