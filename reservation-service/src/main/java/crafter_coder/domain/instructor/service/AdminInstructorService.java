package crafter_coder.domain.instructor.service;

import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.course.model.CourseStatus;
import crafter_coder.domain.course.repository.CourseRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminInstructorService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<InstructorResponseDto> getAllInstructors() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.INSTRUCTIOR)
                .map(InstructorResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InstructorResponseDto getInstructorById(Long id) {
        User instructor = userRepository.findById(id)
                .filter(user -> user.getRole() == Role.INSTRUCTIOR)
                .orElseThrow(() -> new MyException(MyErrorCode.USER_NOT_FOUND));
        return InstructorResponseDto.of(instructor);
    }

    @Transactional
    public void updateCourse(Long instructorId, Long courseId, CourseUpdateRequestDto request) {
        validateInstructor(instructorId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new MyException(MyErrorCode.USER_NOT_FOUND));

        course.updateCourseDetails(
                request.getName(),
                request.getDayOfWeek(),
                request.getStartTime(),
                request.getEndTime(),
                request.getPlace(),
                request.getMaxCapacity()
        );
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
    public void updateCourseStatus(Long instructorId, Long courseId, String status) {
        validateInstructor(instructorId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new MyException(MyErrorCode.COURSE_NOT_FOUND));

        CourseStatus newStatus;
        try {
            newStatus = CourseStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new MyException(MyErrorCode.INVALID_STATUS);
        }

        course.changeStatus(newStatus);
    }

    private void validateInstructor(Long instructorId) {
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new MyException(MyErrorCode.USER_NOT_FOUND));

        if (instructor.getRole() != Role.INSTRUCTIOR) {
            throw new MyException(MyErrorCode.USER_NOT_FOUND);
        }
    }
}
