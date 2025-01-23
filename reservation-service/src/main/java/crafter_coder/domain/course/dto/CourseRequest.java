package crafter_coder.domain.course.dto;

import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.course.model.CourseDuration;
import crafter_coder.domain.course.model.CourseSchedule;
import crafter_coder.domain.course.model.category.CourseSubCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequest {

    private String name;
    private String courseCategory;
    private String startDate;
    private String endDate;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private int maxCapacity;
    private int price;
    private String place;
    private String enrollmentDeadline;

    public Course toEntity(Long instructorId) {
        CourseSubCategory subCategory;
        try {
            subCategory = CourseSubCategory.valueOf(courseCategory.toUpperCase()); // 대소문자 무시 처리
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("강좌 카테고리가 잘못되었습니다.: " + courseCategory, e);
        }

        return Course.create(
                name,
                subCategory,
                CourseDuration.of(LocalDate.parse(startDate), LocalDate.parse(endDate)),
                CourseSchedule.of(dayOfWeek, LocalTime.parse(startTime), LocalTime.parse(endTime)),
                instructorId,
                maxCapacity,
                price,
                place,
                LocalDate.parse(enrollmentDeadline)
        );
    }

}

