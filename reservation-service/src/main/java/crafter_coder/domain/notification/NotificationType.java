package crafter_coder.domain.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    COURSE_OPEN("{courseName} 강좌가 개설되었습니다."),
    AVAILABLE_SEAT("{courseName} 강좌 수강 예약이 가능합니다."),
    COURSE_CANCELLATION("{courseName} 강좌가 정원 미달로 인해 취소되었습니다.");

    private final String content;
}

