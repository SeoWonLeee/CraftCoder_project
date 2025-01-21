package crafter_coder.domain.user_course.repository;

import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.user.model.User;
import crafter_coder.domain.user_course.model.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    List<UserCourse> findByUserId(Long userId);

    boolean existsByCourseAndUser(Course course, User user);
}
