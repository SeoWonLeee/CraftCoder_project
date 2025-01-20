package crafter_coder.domain.course.model;

import crafter_coder.domain.course.dto.CourseRequest;
import crafter_coder.domain.course.model.category.CourseCategory;
import crafter_coder.domain.course.model.category.CourseSubCategory;
import jakarta.persistence.*;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Course {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseSubCategory courseCategory;

    @Embedded
    private CourseDuration courseDuration;

    @Column(nullable = false)
    private CourseSchedule courseSchedule;

    @Column(nullable = false)
    private Long instructorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status;

    @Embedded
    private EnrollmentCapacity enrollmentCapacity;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private LocalDate enrollmentDeadline;

    public void updateCourseDetails(String name, String dayOfWeek, String startTime, String endTime, String place, int maxCapacity) {
        this.name = name;
        this.courseSchedule.updateSchedule(dayOfWeek, startTime, endTime);
        this.place = place;
        this.enrollmentCapacity.updateCapacity(maxCapacity);
    }

    public boolean canBeDeleted() {
        return this.enrollmentCapacity.getCurrentEnrollment() == 0;
    }

    public void updateStatus(String status) {
        this.status = CourseStatus.valueOf(status.toUpperCase());
    }

    private void setFields(String name, CourseSubCategory courseCategory, CourseDuration courseDuration,
                           CourseSchedule courseSchedule, EnrollmentCapacity enrollmentCapacity,
                           int price, String place, LocalDate enrollmentDeadline) {
        this.name = name;
        this.courseCategory = courseCategory;
        this.courseDuration = courseDuration;
        this.courseSchedule = courseSchedule;
        this.enrollmentCapacity = enrollmentCapacity;
        this.price = price;
        this.place = place;
        this.enrollmentDeadline = enrollmentDeadline;
    }

    private Course(String name, CourseSubCategory courseCategory, CourseDuration courseDuration,
                   CourseSchedule courseSchedule, Long instructorId, CourseStatus status,
                   EnrollmentCapacity enrollmentCapacity, int price, String place, LocalDate enrollmentDeadline) {
        this.setFields(name, courseCategory, courseDuration, courseSchedule, enrollmentCapacity, price, place, enrollmentDeadline);
        this.instructorId = instructorId;
        this.status = status;
    }

    public static Course create(String name, CourseSubCategory courseCategory, CourseDuration courseDuration,
                                CourseSchedule courseSchedule, Long instructorId, int maxCapacity, int price,
                                String place, LocalDate enrollmentDeadline) {
        return new Course(
                name,
                courseCategory,
                courseDuration,
                courseSchedule,
                instructorId,
                CourseStatus.OPEN,
                EnrollmentCapacity.of(maxCapacity, 0),
                price,
                place,
                enrollmentDeadline
        );
    }

    public void update(CourseRequest request) {
        this.name = request.getName();
        this.courseCategory = CourseSubCategory.valueOf(request.getCourseCategory());
        this.courseDuration = CourseDuration.of(
                LocalDate.parse(request.getStartDate()),
                LocalDate.parse(request.getEndDate())
        );
        this.courseSchedule = CourseSchedule.of(
                request.getDayOfWeek(),
                LocalTime.parse(request.getStartTime()),
                LocalTime.parse(request.getEndTime())
        );
        this.place = request.getPlace();
        this.price = request.getPrice();
        this.enrollmentCapacity.updateCapacity(request.getMaxCapacity());
        this.enrollmentDeadline = LocalDate.parse(request.getEnrollmentDeadline());
    }

}
