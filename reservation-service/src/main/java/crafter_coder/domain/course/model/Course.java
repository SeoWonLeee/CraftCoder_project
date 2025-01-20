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
    private Long instructorId; // 강사 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status;

    @Embedded
    private EnrollmentCapacity enrollmentCapacity; // 수강 정원

    @Column(nullable = false)
    private int price; // 수강료

    @Column(nullable = false)
    private String place; // 강좌 장소

    @Column(nullable = false)
    private LocalDate enrollmentDeadline; // 수강신청 데드라인

    // 공통 필드 설정
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
                CourseStatus.OPEN, // 초기 상태는 OPEN
                EnrollmentCapacity.of(maxCapacity, 0), // 초기 등록 인원은 0
                price,
                place,
                enrollmentDeadline
        );
    }

    // 수정 메서드
    public void update(CourseRequest request) {
        this.setFields(
                request.getName(),
                CourseSubCategory.valueOf(request.getCourseCategory()),
                CourseDuration.of(LocalDate.parse(request.getStartDate()), LocalDate.parse(request.getEndDate())),
                CourseSchedule.of(request.getDayOfWeek(), LocalTime.parse(request.getStartTime()), LocalTime.parse(request.getEndTime())),
                EnrollmentCapacity.of(request.getMaxCapacity(), this.enrollmentCapacity.getCurrentEnrollment()),
                request.getPrice(),
                request.getPlace(),
                LocalDate.parse(request.getEnrollmentDeadline())
        );
    }
}
