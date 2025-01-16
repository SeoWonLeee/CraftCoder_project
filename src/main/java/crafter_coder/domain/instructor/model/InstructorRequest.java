package crafter_coder.domain.instructor.model;

import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class InstructorRequest {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType type;

    @Column(name = "requested_at", nullable = false)
    private LocalDate requestedAt;

    @Column(nullable = true)
    private String reason;

    private String status;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private Integer maxCapacity;
    private LocalDate startDate;
    private LocalDate endDate;

    public static InstructorRequest createUpdateRequest(User instructor, Course course, String status, String dayOfWeek,
                                                        String startTime, String endTime, Integer maxCapacity,
                                                        LocalDate startDate, LocalDate endDate) {
        InstructorRequest request = new InstructorRequest();
        request.instructor = instructor;
        request.course = course;
        request.type = RequestType.UPDATE;
        request.requestedAt = LocalDate.now();
        request.status = status;
        request.dayOfWeek = dayOfWeek;
        request.startTime = startTime;
        request.endTime = endTime;
        request.maxCapacity = maxCapacity;
        request.startDate = startDate;
        request.endDate = endDate;
        return request;
    }

    public static InstructorRequest createDeleteRequest(User instructor, Course course, String reason) {
        InstructorRequest request = new InstructorRequest();
        request.instructor = instructor;
        request.course = course;
        request.type = RequestType.DELETE;
        request.requestedAt = LocalDate.now();
        request.reason = reason;
        return request;
    }
}
