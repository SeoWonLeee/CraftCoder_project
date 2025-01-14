// Repository Interface
package crafter_coder.domain.course.repository;

import crafter_coder.domain.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
