package crafter_coder.domain.instructor.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseStatusComparisonDto {
    private final String oldStatus;
    private final String newStatus;
}
