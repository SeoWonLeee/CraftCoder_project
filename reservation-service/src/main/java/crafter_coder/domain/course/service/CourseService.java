package crafter_coder.domain.course.service;

import crafter_coder.domain.course.dto.CourseDetailResponse;
import crafter_coder.domain.course.dto.CourseListResponse;
import crafter_coder.domain.course.dto.CourseRequest;
import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.course.repository.CourseRepository;
import crafter_coder.global.redis.AddQueueResponse;
import crafter_coder.global.redis.RedisQueueFacade;
import crafter_coder.domain.notification.NotificationDto;
import crafter_coder.domain.notification.NotificationType;
import crafter_coder.domain.notification.event.NotificationEvent;
import crafter_coder.domain.notification.event.NotificationEventPublisher;
import crafter_coder.domain.user.dto.UserResponseDto;
import crafter_coder.domain.user_course.model.UserCourse;
import crafter_coder.global.exception.MyErrorCode;
import crafter_coder.global.exception.MyException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final RedisQueueFacade redisQueueFacade;
    private final NotificationEventPublisher notificationEventPublisher;

    //강좌 신청하기
    public AddQueueResponse applyCourse(Long courseId, String userId) {

        return redisQueueFacade.addApplyToWaitingQueue(courseId, userId);
    }

    //전체 강좌 목록 가져오기
    public List<CourseListResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(CourseListResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //강좌 상세 가져오기
    public Optional<CourseDetailResponse> getCourseDetail(Long courseId) {
        return courseRepository.findById(courseId)
                .map(CourseDetailResponse::fromEntity);
    }

    // 강좌 생성
    public Long createCourse(CourseRequest request, Long instructorId) {
        Course course = request.toEntity(instructorId);
        courseRepository.save(course);

        String content = NotificationType.COURSE_OPEN.getContent().replace("{courseName}", course.getName());
        // 특정 사용자가 아닌 전체 사용자에게 알림을 보낼 것이므로 receiverId를 null로 설정
        notificationEventPublisher.publish(NotificationEvent.of(this, NotificationDto.of(content, NotificationType.COURSE_OPEN, null)));
        return course.getId();
    }

    // 강좌 수정
    public void updateCourse(Long courseId, CourseRequest request, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("해당 강좌를 찾을 수 없습니다."));
        if (!course.getInstructorId().equals(instructorId)) {
            throw new AccessDeniedException("해당 강좌를 수정할 권한이 없습니다.");
        }
        course.update(request);
    }

    // 결제 완료
    public void completePayment(Long courseId, String userId) {

        redisQueueFacade.completePayment(courseId, userId);
    }

    // 결제 취소
    public void cancelPayment(Long courseId, String userId) {

        redisQueueFacade.cancelPayment(courseId, userId);
    }
  
  
    // dev에 있던 내용 (필요없다면 삭제하기)
//    course.changeStatus(newStatus);
//    courseRepository.save(course);

}



