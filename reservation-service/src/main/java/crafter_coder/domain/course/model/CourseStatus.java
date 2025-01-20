package crafter_coder.domain.course.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CourseStatus {
    OPEN,
    CLOSED,
    CANCELED,
    ;
}
