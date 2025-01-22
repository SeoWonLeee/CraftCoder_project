package crafter_coder.domain.notification.service;

import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.course.model.CourseDuration;
import crafter_coder.domain.course.model.CourseSchedule;
import crafter_coder.domain.course.model.category.CourseSubCategory;
import crafter_coder.domain.course.repository.CourseRepository;
import crafter_coder.domain.notification.NotificationDto;
import crafter_coder.domain.notification.NotificationType;
import crafter_coder.domain.notification.event.NotificationEvent;
import crafter_coder.domain.notification.util.IdTimestampUtil;
import crafter_coder.domain.user.model.ActiveStatus;
import crafter_coder.domain.user.model.Role;
import crafter_coder.domain.user.model.User;
import crafter_coder.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class NotificationSenderTest {

    @Autowired
    private NotificationSender notificationSender;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private IdTimestampUtil idTimestampUtil;

    @AfterEach
    void after() {
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("강좌 개설 시 전체 유저에게 알림을 보낸다.")
    void sendNotification_course_opened() {
        // given
        User user1 = User.of("gildong", "asdf", "홍길동", "01012345678", LocalDate.of(1990, 1, 1), "1234421421412", "dsada", Role.USER, ActiveStatus.ACTIVE, "da");
        User user2 = User.of("sangho", "asdf", "김상호", "01012345678", LocalDate.of(1990, 1, 1), "1234421421412", "dsada", Role.USER, ActiveStatus.ACTIVE, "da");

        userRepository.saveAll(List.of(user1, user2));

        String courseName = "스프링 완전정복";
        Course course = Course.create(
                courseName,
                CourseSubCategory.CHILD_EDUCATION,
                CourseDuration.of(LocalDate.of(2025, 3, 1), LocalDate.of(2025, 6, 30)),
                CourseSchedule.of("MONDAY", LocalTime.of(10, 0), LocalTime.of(12, 0)),
                1L,
                30,
                200000,
                "코딩학원",
                LocalDate.of(2025, 2, 1)
        );
        courseRepository.save(course);

        String content = NotificationType.COURSE_OPEN.getContent().replace("{courseName}", courseName);
        NotificationEvent event = NotificationEvent.of(this, NotificationDto.of(content, NotificationType.COURSE_OPEN, null));

        // 클라이언트가 sse 구독 요청을 보냈다고 가정
        String emitterId1 = idTimestampUtil.makeTimeIncludeId(user1.getId());
        String emitterId2 = idTimestampUtil.makeTimeIncludeId(user2.getId());
        notificationService.subscribe(emitterId1, null);
        notificationService.subscribe(emitterId2, null);

        // when
        notificationSender.sendNotification(event);

        // then
        // user1, user2에게 강좌 개설 알림이 전송되었는지 확인해야 하는데...Mocking으로 send 호출했는지 확인 정돈 할 수 있을거 같은데 실제로 동작하는지 테스트하긴 까다롭...
    }
}