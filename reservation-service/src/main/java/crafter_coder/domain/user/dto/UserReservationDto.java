package crafter_coder.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserReservationDto {
    private Long courseId;
    private String courseName;
    private String status;
    private String paymentStatus;
    private LocalDate reservationDate;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private String place;
    private String instructorName;
    private LocalDate startDate;
    private LocalDate endDate;
}
