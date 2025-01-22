package crafter_coder.domain.user_course.repository;

import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.user.model.User;
import crafter_coder.domain.user_course.model.UserCourse;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    // 특정 사용자가 신청한 모든 강좌 가져오기
    List<UserCourse> findByUserId(Long userId);

    // 특정 강좌와 사용자 간의 UserCourse 존재 여부 확인
    boolean existsByCourseAndUser(Course course, User user);

    // 특정 강좌와 사용자에 대한 UserCourse 가져오기 (상태 업데이트 등에서 사용)
    Optional<UserCourse> findByCourseAndUser(Course course, User user);

    // ksah3756: 강좌 정원 미달로 폐강 시 해당 강좌를 수강한 사용자들 조회해서 알림을 보내야 하기 때문에 추가
    List<UserCourse> findByCourseId(Long courseId);
}
