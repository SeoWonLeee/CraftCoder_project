package crafter_coder.domain.instructor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CourseUpdateRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    private String dayOfWeek;

    @NotBlank
    private String startTime;

    @NotBlank
    private String endTime;

    @NotBlank
    private String place;

    @NotNull
    private Integer maxCapacity;
}
