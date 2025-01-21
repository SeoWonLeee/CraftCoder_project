package crafter_coder.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserCourseReservationDto {
    private Long courseId;
    private String courseName;
    private String status;
    private String paymentStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private String instructorName;
}
