package crafter_coder.domain.user_course.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnrollmentStatus {
    APPLICATED, // 신청됨
    PAYED,      // 결제 완료
    CANCELED,   // 취소됨
    ;
}
