package crafter_coder.domain.course.model.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CourseCategory {
    CULTURE("교양"),
    HOBBY("취미"),
    LANGUAGE("외국어"),
    PARENTING("육아"),
    ;

    private final String description; // 한글 표현
}
