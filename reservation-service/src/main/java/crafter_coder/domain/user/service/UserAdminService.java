package crafter_coder.domain.user.service;

import crafter_coder.domain.user.dto.UserRecordResponseDto;
import crafter_coder.domain.user.dto.UserReservationDto;
import crafter_coder.domain.user.dto.UserResponseDto;
import crafter_coder.domain.user.model.ActiveStatus;
import crafter_coder.domain.user.model.Role;
import crafter_coder.domain.user.model.User;
import crafter_coder.domain.user.repository.UserRepository;
import crafter_coder.domain.user_course.model.UserCourse;
import crafter_coder.domain.user_course.repository.UserCourseRepository;
import crafter_coder.global.exception.MyErrorCode;
import crafter_coder.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;
    private final UserCourseRepository userCourseRepository;

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new MyException(MyErrorCode.USER_NOT_FOUND));
        return UserResponseDto.of(user);
    }

    @Transactional(readOnly = true)
    public UserRecordResponseDto getUserRecords(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new MyException(MyErrorCode.USER_NOT_FOUND));

        List<UserReservationDto> reservations = fetchUserReservations(user.getId());
        return UserRecordResponseDto.of(user, reservations);
    }

    private List<UserReservationDto> fetchUserReservations(Long userId) {
        return userCourseRepository.findByUserId(userId).stream()
                .map(this::mapToReservationDto)
                .collect(Collectors.toList());
    }

    private UserReservationDto mapToReservationDto(UserCourse userCourse) {
        return UserReservationDto.builder()
                .courseId(userCourse.getCourse().getId())
                .courseName(userCourse.getCourse().getName())
                .status(userCourse.getEnrollmentStatus().name())
                .paymentStatus("DONE") // 결제 상태 연동 필요
                .reservationDate(userCourse.getPaymentDeadline())
                .dayOfWeek(userCourse.getCourse().getCourseSchedule().getDayOfWeek())
                .startTime(userCourse.getCourse().getCourseSchedule().getStartTime().toString())
                .endTime(userCourse.getCourse().getCourseSchedule().getEndTime().toString())
                .place(userCourse.getCourse().getPlace())
                .instructorName(fetchInstructorName(userCourse.getCourse().getInstructorId()))
                .startDate(userCourse.getCourse().getCourseDuration().getStartDate())
                .endDate(userCourse.getCourse().getCourseDuration().getEndDate())
                .build();
    }

    @Transactional
    public void updateUserStatus(Long id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new MyException(MyErrorCode.USER_NOT_FOUND));
        user.updateStatus(ActiveStatus.valueOf(status.toUpperCase()));
    }

    private String fetchInstructorName(Long instructorId) {
        User instructor = userRepository.findByIdAndRole(instructorId, Role.INSTRUCTIOR);
        if (instructor == null) {
            throw new MyException(MyErrorCode.USER_NOT_FOUND);
        }
        return instructor.getName();
    }
}
