package crafter_coder.domain.instructor.service;

import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.course.repository.CourseRepository;
import crafter_coder.domain.instructor.dto.InstructorRequestDto;
import crafter_coder.domain.instructor.model.InstructorRequest;
import crafter_coder.domain.instructor.repository.InstructorRequestRepository;
import crafter_coder.domain.user.model.Role;
import crafter_coder.domain.user.model.User;
import crafter_coder.domain.user.repository.UserRepository;
import crafter_coder.global.exception.MyErrorCode;
import crafter_coder.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstructorRequestService {

    private final InstructorRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public void createUpdateRequest(Long instructorId, Long courseId, String status, String dayOfWeek, String startTime,
                                    String endTime, Integer maxCapacity, LocalDate startDate, LocalDate endDate) {
        User instructor = validateInstructor(instructorId);
        validateCourseOwnership(instructorId, courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new MyException(MyErrorCode.COURSE_NOT_FOUND));
        InstructorRequest request = InstructorRequest.createUpdateRequest(
                instructor, course, status, dayOfWeek, startTime, endTime, maxCapacity, startDate, endDate);
        requestRepository.save(request);
    }

    @Transactional
    public void createDeleteRequest(Long instructorId, Long courseId, String reason) {
        User instructor = validateInstructor(instructorId);
        validateCourseOwnership(instructorId, courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new MyException(MyErrorCode.COURSE_NOT_FOUND));
        InstructorRequest request = InstructorRequest.createDeleteRequest(instructor, course, reason);
        requestRepository.save(request);
    }

    @Transactional
    public void completeRequest(Long requestId) {
        InstructorRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new MyException(MyErrorCode.REQUEST_NOT_FOUND));
        request.markAsCompleted();
    }

    @Transactional
    public void approveRequest(Long requestId) {
        InstructorRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new MyException(MyErrorCode.REQUEST_NOT_FOUND));
        request.approve();
    }

    @Transactional
    public void rejectRequest(Long requestId, String reason) {
        InstructorRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new MyException(MyErrorCode.REQUEST_NOT_FOUND));
        request.reject(reason);
    }

    @Transactional(readOnly = true)
    public List<InstructorRequestDto> getAllRequests(String status) {
        List<InstructorRequest> requests = (status == null)
                ? requestRepository.findAll()
                : requestRepository.findByStatus(status);

        return requests.stream()
                .map(InstructorRequestDto::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InstructorRequestDto getRequestDetail(Long requestId) {
        InstructorRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new MyException(MyErrorCode.REQUEST_NOT_FOUND));
        return InstructorRequestDto.of(request);
    }

    @Transactional(readOnly = true)
    public List<InstructorRequest> getRequestsByInstructor(Long instructorId) {
        User instructor = validateInstructor(instructorId);
        return requestRepository.findByInstructor(instructor);
    }

    private User validateInstructor(Long instructorId) {
        return userRepository.findById(instructorId)
                .filter(user -> user.getRole() == Role.INSTRUCTIOR)
                .orElseThrow(() -> new MyException(MyErrorCode.INSTRUCTOR_ONLY));
    }

    private void validateCourseOwnership(Long instructorId, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new MyException(MyErrorCode.COURSE_NOT_FOUND));
        if (!course.getInstructorId().equals(instructorId)) {
            throw new MyException(MyErrorCode.INSTRUCTOR_ONLY);
        }
    }
}