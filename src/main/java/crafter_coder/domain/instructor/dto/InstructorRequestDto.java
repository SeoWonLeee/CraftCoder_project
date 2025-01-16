package crafter_coder.domain.instructor.dto;

import crafter_coder.domain.instructor.model.InstructorRequest;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class InstructorRequestDto {

    private Long requestId;
    private String type;
    private Long courseId;
    private String courseName;
    private String status;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private Integer maxCapacity;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LocalDate requestedAt;

    public static InstructorRequestDto of(InstructorRequest request) {
        return InstructorRequestDto.builder()
                .requestId(request.getId())
                .type(request.getType().name())
                .courseId(request.getCourse().getId())
                .courseName(request.getCourse().getName())
                .status(request.getStatus())
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .maxCapacity(request.getMaxCapacity())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .requestedAt(request.getRequestedAt())
                .build();
    }
}
