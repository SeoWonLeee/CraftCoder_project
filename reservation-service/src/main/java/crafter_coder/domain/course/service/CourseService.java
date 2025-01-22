package crafter_coder.domain.course.service;

import crafter_coder.domain.course.dto.CourseDetailResponse;
import crafter_coder.domain.course.dto.CourseListResponse;
import crafter_coder.domain.course.dto.CourseRequest;
import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.course.model.CourseStatus;
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
import org.springframework.transaction.annotation.Transactional;
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

    public AddQueueResponse applyCourse(Long courseId, String userId) {
        return redisQueueFacade.addApplyToWaitingQueue(courseId, userId);
    }

    public void completePayment(Long courseId, String userId) {
        redisQueueFacade.completePayment(courseId, userId);
    }

    public void cancelPayment(Long courseId, String userId) {
        redisQueueFacade.cancelPayment(courseId, userId);
    }

    public List<CourseListResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(CourseListResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<CourseDetailResponse> getCourseDetail(Long courseId) {
        return courseRepository.findById(courseId)
                .map(CourseDetailResponse::fromEntity);
    }

    public Long createCourse(CourseRequest request, Long instructorId) {
        Course course = request.toEntity(instructorId);
        courseRepository.save(course);

        String content = course.getName() + " 강좌가 개설되었습니다.";
        notificationEventPublisher.publish(NotificationEvent.of(this, NotificationDto.of(content, NotificationType.COURSE_OPEN, null)));
        return course.getId();
    }

    public void updateCourse(Long courseId, CourseRequest request, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("해당 강좌를 찾을 수 없습니다."));
        if (!course.getInstructorId().equals(instructorId)) {
            throw new AccessDeniedException("해당 강좌를 수정할 권한이 없습니다.");
        }
        course.update(request);
    }

    @Transactional(readOnly = true)
    public List<CourseListResponse> getCoursesByCategory(String category) {
        List<Course> courses = courseRepository.findByCategory(category);
        return courses.stream()
                .map(CourseListResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getCourseUsers(Long courseId) {
        List<UserCourse> userCourses = courseRepository.findByCourseId(courseId);
        if (userCourses.isEmpty()) {
            throw new MyException(MyErrorCode.COURSE_NOT_FOUND);
        }
        return userCourses.stream()
                .map(userCourse -> UserResponseDto.of(userCourse.getUser()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateCourseStatus(Long courseId, String status) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new MyException(MyErrorCode.COURSE_NOT_FOUND));

        CourseStatus newStatus;
        try {
            newStatus = CourseStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new MyException(MyErrorCode.INVALID_STATUS);
        }

        course.changeStatus(newStatus);
        courseRepository.save(course);
    }
}
