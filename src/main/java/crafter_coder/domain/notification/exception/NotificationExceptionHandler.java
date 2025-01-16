package crafter_coder.domain.notification.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotificationExceptionHandler {

    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<NotificationErrorCode> handleNotificationException(NotificationException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode());
    }
}
