package crafter_coder.domain.course.repository;

import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.user_course.model.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE c.courseCategory IN :subCategories")
    List<Course> findBySubCategories(@Param("subCategories") List<String> subCategories);

    @Query("SELECT c FROM Course c WHERE LOWER(c.courseCategory) = LOWER(:category)")
    List<Course> findByCategory(@Param("category") String category);

    @Query("SELECT uc FROM UserCourse uc WHERE uc.course.id = :courseId")
    List<UserCourse> findByCourseId(@Param("courseId") Long courseId);
}