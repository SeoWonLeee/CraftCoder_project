package crafter_coder.domain.notification.exception;

import lombok.Getter;

@Getter
public class NotificationException extends RuntimeException {
    private NotificationErrorCode errorCode;

    public NotificationException(NotificationErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static class NotificationSendException extends NotificationException {
        public NotificationSendException() {
            super(NotificationErrorCode.NOTIFICATION_NOT_SENT);
        }
    }
}
