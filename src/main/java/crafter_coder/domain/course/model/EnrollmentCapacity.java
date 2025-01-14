package crafter_coder.domain.course.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Embeddable
@Getter
@NoArgsConstructor(access = PROTECTED)
public class EnrollmentCapacity {

    @Column(nullable = false)
    private int maxCapacity; // 최대 수강 인원

    @Column(nullable = false)
    private int currentEnrollment; // 현재 수강 인원

    private EnrollmentCapacity(int maxCapacity, int currentEnrollment) {
        this.maxCapacity = maxCapacity;
        this.currentEnrollment = currentEnrollment;
    }

    public static EnrollmentCapacity of(int maxCapacity, int currentEnrollment) {
        return new EnrollmentCapacity(maxCapacity, currentEnrollment);
    }
}
