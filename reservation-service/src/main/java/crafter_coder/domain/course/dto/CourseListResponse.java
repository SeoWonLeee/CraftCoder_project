package crafter_coder.domain.course.dto;

import crafter_coder.domain.course.model.Course;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseListResponse {
    private final Long id;
    private final String name;
    private final String startDate;
    private final String endDate;
    private final String dayOfWeek;
    private final String startTime;
    private final String endTime;
    private final Long instructorId;
    private final String status;
    private final int price;
    private final int maxCapacity;
    private final String enrollmentDeadline;

    public static CourseListResponse fromEntity(Course course) {
        return CourseListResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .startDate(course.getCourseDuration().getStartDate().toString())
                .endDate(course.getCourseDuration().getEndDate().toString())
                .dayOfWeek(course.getCourseSchedule().getDayOfWeek())
                .startTime(course.getCourseSchedule().getStartTime().toString())
                .endTime(course.getCourseSchedule().getEndTime().toString())
                .instructorId(course.getInstructorId())
                .status(course.getStatus().name())
                .price(course.getPrice())
                .maxCapacity(course.getEnrollmentCapacity().getMaxCapacity())
                .enrollmentDeadline(course.getEnrollmentDeadline().toString())
                .build();
    }
}
