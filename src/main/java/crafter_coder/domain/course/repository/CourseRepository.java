package crafter_coder.domain.course.repository;

import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.course.model.category.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCourseCategory(CourseCategory courseCategory);
}
