package crafter_coder.domain.notification.service;

import crafter_coder.domain.notification.NotificationDto;
import crafter_coder.domain.notification.NotificationType;
import crafter_coder.domain.notification.emitter.EmitterRepository;
import crafter_coder.domain.notification.event.NotificationEvent;
import crafter_coder.domain.notification.util.IdTimestampUtil;
import crafter_coder.domain.user_course.repository.UserCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static crafter_coder.domain.notification.NotificationType.*;

@Service
@RequiredArgsConstructor
public class NotificationSender {

    private final NotificationService notificationService;
    private final EmitterRepository emitterRepository;
    private final UserCourseRepository userCourseRepository;
    private final IdTimestampUtil idTimestampUtil;

    public void sendNotification(NotificationEvent event) {
        NotificationDto notificationDto = event.getNotificationDto();
        NotificationType notificationType = notificationDto.notificationType();

        if(notificationType.equals(COURSE_OPEN)) { // COURSE_OPEN은 모든 사용자에게 알림
            emitterRepository.getAllEmitters().forEach((key, value) -> {
                Long userId = idTimestampUtil.extractUserId(key);
                sendNotificationToTargetUser(userId, notificationType, notificationDto);
            });
        } else if(notificationType.equals(COURSE_CANCELLATION)) { // COURSE_CANCELLATION은 해당 강좌를 수강했던 사용자에게 알림
            // userCourse에서 해당 강좌를 수강한 사용자들을 조회하여 알림 전송 (NotifictionDto.receiverId()는 courseId)
            Long courseId = notificationDto.receiverId();
            userCourseRepository.findByCourseId(courseId).forEach(userCourse -> {
                Long userId = userCourse.getUser().getId();
                sendNotificationToTargetUser(userId, notificationType, notificationDto);
            });
        } else if(notificationType.equals(AVAILABLE_SEAT)) { // COURSE_UPDATE은 대기열 첫 순번 사용자에게 알림
            sendNotificationToTargetUser(notificationDto.receiverId(), notificationType, notificationDto);
        }
    }

    private void sendNotificationToTargetUser(Long userId, NotificationType notificationType, NotificationDto notificationDto) {
        String eventId = idTimestampUtil.makeTimeIncludeId(userId);
        notificationService.send(userId, eventId, notificationType, notificationDto.content());
    }
}
