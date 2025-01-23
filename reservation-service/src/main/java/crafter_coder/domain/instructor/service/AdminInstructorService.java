package crafter_coder.domain.instructor.service;

import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.course.model.CourseStatus;
import crafter_coder.domain.course.repository.CourseRepository;
import crafter_coder.domain.instructor.dto.CourseStatusComparisonDto;
import crafter_coder.domain.instructor.dto.CourseUpdateComparisonDto;
import crafter_coder.domain.instructor.dto.CourseUpdateRequestDto;
import crafter_coder.domain.instructor.dto.InstructorResponseDto;
import crafter_coder.domain.user.model.Role;
import crafter_coder.domain.user.model.User;
import crafter_coder.domain.user.repository.UserRepository;
import crafter_coder.global.exception.MyErrorCode;
import crafter_coder.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminInstructorService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<InstructorResponseDto> getAllInstructors() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.INSTRUCTOR)
                .map(InstructorResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InstructorResponseDto getInstructorById(Long id) {
        User instructor = userRepository.findById(id)
                .filter(user -> user.getRole() == Role.INSTRUCTOR)
                .orElseThrow(() -> new MyException(MyErrorCode.USER_NOT_FOUND));
        return InstructorResponseDto.of(instructor);
    }

    @Transactional
    public void deleteCourse(Long instructorId, Long courseId) {
        validateInstructor(instructorId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new MyException(MyErrorCode.USER_NOT_FOUND));

        if (!course.canBeDeleted()) {
            throw new MyException(MyErrorCode.COURSE_CANNOT_BE_DELETED);
        }

        courseRepository.delete(course);
    }

    @Transactional
    public CourseStatusComparisonDto updateCourseStatus(Long instructorId, Long courseId, String status) {
        validateInstructor(instructorId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new MyException(MyErrorCode.COURSE_NOT_FOUND));
        CourseStatus oldStatus = course.getStatus();

        CourseStatus newStatus;
        try {
            newStatus = CourseStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new MyException(MyErrorCode.INVALID_STATUS);
        }

        course.changeStatus(newStatus);

        return CourseStatusComparisonDto.builder()
                .oldStatus(oldStatus.name())
                .newStatus(newStatus.name())
                .build();
    }

    private void validateInstructor(Long instructorId) {
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new MyException(MyErrorCode.USER_NOT_FOUND));

        if (instructor.getRole() != Role.INSTRUCTOR) {
            throw new MyException(MyErrorCode.USER_NOT_FOUND);
        }
    }

    @Transactional
    public CourseUpdateComparisonDto updateCourse(Long instructorId, Long courseId, CourseUpdateRequestDto request) {
        validateInstructor(instructorId);

        Course originalCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new MyException(MyErrorCode.COURSE_NOT_FOUND));
        Map<String, Object> oldData = Map.of(
                "name", originalCourse.getName(),
                "dayOfWeek", originalCourse.getCourseSchedule().getDayOfWeek(),
                "startTime", originalCourse.getCourseSchedule().getStartTime().toString(),
                "endTime", originalCourse.getCourseSchedule().getEndTime().toString(),
                "place", originalCourse.getPlace(),
                "maxCapacity", originalCourse.getEnrollmentCapacity().getMaxCapacity()
        );

        originalCourse.updateCourseDetails(
                request.getName(),
                request.getDayOfWeek(),
                request.getStartTime(),
                request.getEndTime(),
                request.getPlace(),
                request.getMaxCapacity()
        );

        Map<String, Object> newData = Map.of(
                "name", originalCourse.getName(),
                "dayOfWeek", originalCourse.getCourseSchedule().getDayOfWeek(),
                "startTime", originalCourse.getCourseSchedule().getStartTime().toString(),
                "endTime", originalCourse.getCourseSchedule().getEndTime().toString(),
                "place", originalCourse.getPlace(),
                "maxCapacity", originalCourse.getEnrollmentCapacity().getMaxCapacity()
        );

        return CourseUpdateComparisonDto.builder()
                .oldData(oldData)
                .newData(newData)
                .build();
    }
}
