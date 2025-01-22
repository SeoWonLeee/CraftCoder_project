package crafter_coder.domain.course.service;

import crafter_coder.domain.course.dto.CourseDetailResponse;
import crafter_coder.domain.course.dto.CourseListResponse;
import crafter_coder.domain.course.dto.CourseRequest;
import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.course.model.CourseStatus;
import crafter_coder.domain.course.model.category.CourseCategory;
import crafter_coder.domain.course.model.category.CourseSubCategory;
import crafter_coder.domain.course.repository.CourseRepository;
import crafter_coder.domain.user.dto.UserResponseDto;
import crafter_coder.domain.user_course.model.UserCourse;
import crafter_coder.global.exception.MyErrorCode;
import crafter_coder.global.exception.MyException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

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
        return course.getId();
    }

    public void updateCourse(Long courseId, CourseRequest request, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("해당 강좌를 찾을 수 없습니다."));
        if (!course.getInstructorId().equals(instructorId)) {
        }
        course.update(request);
    }

    @Transactional(readOnly = true)
    public List<CourseListResponse> getCoursesByCategory(String category) {
        List<Course> courses = fetchCoursesByCategory(category);
        return courses.stream()
                .map(CourseListResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private List<Course> fetchCoursesByCategory(String category) {
        if (isTopCategory(category)) {
            return getCoursesByTopCategory(category);
        } else if (isSubCategory(category)) {
            return getCoursesBySubCategory(category);
        } else {
            throw new MyException(MyErrorCode.INVALID_CATEGORY);
        }
    }

    private boolean isTopCategory(String category) {
        return Arrays.stream(CourseCategory.values())
                .anyMatch(top -> top.name().equalsIgnoreCase(category));
    }

    private boolean isSubCategory(String category) {
        return Arrays.stream(CourseSubCategory.values())
                .anyMatch(sub -> sub.name().equalsIgnoreCase(category));
    }

    private List<Course> getCoursesByTopCategory(String category) {
        CourseCategory topCategory = CourseCategory.valueOf(category.toUpperCase());
        List<String> subCategories = Arrays.stream(CourseSubCategory.values())
                .filter(sub -> sub.getCategory() == topCategory)
                .map(Enum::name)
                .collect(Collectors.toList());
        return courseRepository.findBySubCategories(subCategories);
    }

    private List<Course> getCoursesBySubCategory(String category) {
        return courseRepository.findByCategory(category.toUpperCase());
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
