package crafter_coder.domain.notification.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationErrorCode {
    NOTIFICATION_NOT_SENT(HttpStatus.INTERNAL_SERVER_ERROR,"알림 전송에 실패하였습니다.");

    private final HttpStatus status;
    private final String message;
}
