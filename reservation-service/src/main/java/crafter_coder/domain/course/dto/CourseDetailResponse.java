package crafter_coder.domain.course.dto;

import crafter_coder.domain.course.model.Course;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseDetailResponse {
    private final Long id;
    private final String name;
    private final String courseCategory;
    private final String startDate;
    private final String endDate;
    private final String dayOfWeek;
    private final String startTime;
    private final String endTime;
    private final Long instructorId;
    private final String status;
    private final int price;
    private final String place;
    private final int maxCapacity;
    private final int currentEnrollment;
    private final String enrollmentDeadline;

    public static CourseDetailResponse fromEntity(Course course) {
        return CourseDetailResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .courseCategory(course.getCourseCategory().name())
                .startDate(course.getCourseDuration().getStartDate().toString())
                .endDate(course.getCourseDuration().getEndDate().toString())
                .dayOfWeek(course.getCourseSchedule().getDayOfWeek())
                .startTime(course.getCourseSchedule().getStartTime().toString())
                .endTime(course.getCourseSchedule().getEndTime().toString())
                .instructorId(course.getInstructorId())
                .status(course.getStatus().name())
                .price(course.getPrice())
                .place(course.getPlace())
                .maxCapacity(course.getEnrollmentCapacity().getMaxCapacity())
                .currentEnrollment(course.getEnrollmentCapacity().getCurrentEnrollment())
                .enrollmentDeadline(course.getEnrollmentDeadline().toString())
                .build();
    }
}
