package crafter_coder.domain.course.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class UpdateCourseRequest {

    @NotNull
    private final String name;

    @NotNull
    private final String courseCategory;

    @NotNull
    private final LocalDate startDate;

    @NotNull
    private final LocalDate endDate;

    @NotNull
    private final String dayOfWeek;

    @NotNull
    private final LocalTime startTime;

    @NotNull
    private final LocalTime endTime;

    @NotNull
    private final Long instructorId;

    @NotNull
    private final String status;

    @NotNull
    private final int price;

    @NotNull
    private final String place;

    @NotNull
    private final int maxCapacity;

    @NotNull
    private final int currentEnrollment;

    @NotNull
    private final LocalDate enrollmentDeadline;

}

