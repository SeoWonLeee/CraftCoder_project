package crafter_coder.domain.user_course.model;

import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class UserCourse {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus enrollmentStatus;

    @Column(name = "payment_deadline", nullable = false)
    private LocalDate paymentDeadline;

    private UserCourse(User user, Course course, EnrollmentStatus enrollmentStatus, LocalDate paymentDeadline) {
        this.user = user;
        this.course = course;
        this.enrollmentStatus = enrollmentStatus;
        this.paymentDeadline = paymentDeadline;
    }

    public static UserCourse of(User user, Course course) {
        LocalDate enrollmentDeadline = course.getEnrollmentDeadline();
        return new UserCourse(user, course, EnrollmentStatus.APPLICATED, enrollmentDeadline);
    }

    // 상태 업데이트 메서드 추가
    public void updateStatus(EnrollmentStatus newStatus) {
        if (this.enrollmentStatus == newStatus) {
            throw new IllegalStateException("이미 해당 상태입니다.");
        }
        this.enrollmentStatus = newStatus;
    }
}
