package crafter_coder.domain.course.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.*;

@Embeddable @Getter
@NoArgsConstructor(access = PROTECTED)
public class CourseDuration {

    private LocalDate startDate;
    private LocalDate endDate;


    private CourseDuration(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static CourseDuration of(LocalDate startDate, LocalDate endDate) {
        return new CourseDuration(startDate, endDate);
    }
}
