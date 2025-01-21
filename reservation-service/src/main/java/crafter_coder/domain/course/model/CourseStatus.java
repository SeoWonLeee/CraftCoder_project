package crafter_coder.domain.course.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CourseStatus {
    OPEN,     // 강좌 진행중
    CLOSED,   // 모집 인원 마감
    CANCELED, // 강좌 모집 취소
    WAITING;  // 대기자 상태

    public boolean canChangeTo(CourseStatus newStatus) {
        if (this == CANCELED) {
            return false;
        }
        return true;
    }
}
