package crafter_coder.domain.course.model;

import crafter_coder.domain.course.model.category.CourseCategory;
import jakarta.persistence.*;
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
    private CourseCategory courseCategory;

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
}
