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
    private LocalTime time;

    private CourseSchedule(String dayOfWeek, LocalTime time) {
        this.dayOfWeek = dayOfWeek;
        this.time = time;
    }

    public static CourseSchedule of(String dayOfWeek, LocalTime time) {
        return new CourseSchedule(dayOfWeek, time);
    }
}
