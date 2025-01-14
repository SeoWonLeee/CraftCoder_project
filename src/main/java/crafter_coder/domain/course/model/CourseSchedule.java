package crafter_coder.domain.course.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

import static lombok.AccessLevel.*;

@Embeddable @Getter
@NoArgsConstructor(access = PROTECTED)
public class CourseSchedule {

    @Column(nullable = false)
    private String dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    private CourseSchedule(String dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static CourseSchedule of(String dayOfWeek, LocalTime startTime, LocalTime endTime) {
        return new CourseSchedule(dayOfWeek, startTime, endTime);
    }
}
