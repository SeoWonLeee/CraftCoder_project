package crafter_coder.domain.instructor.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class CourseUpdateComparisonDto {
    private Map<String, Object> oldData;
    private Map<String, Object> newData;
}

