package crafter_coder.domain.instructor.dto;

import crafter_coder.domain.instructor.model.InstructorRequest;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class InstructorRequestDto {

    private String status;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private Integer maxCapacity;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;

    public static InstructorRequestDto of(InstructorRequest request) {
        return InstructorRequestDto.builder()
                .status(request.getStatus())
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .maxCapacity(request.getMaxCapacity())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .build();
    }
}
