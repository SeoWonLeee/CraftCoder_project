package crafter_coder.domain.notification;

public record NotificationDto(
        String content,
        NotificationType notificationType,
        Long receiverId
) {
    public static NotificationDto of(String content, NotificationType notificationType, Long receiverId) {
        return new NotificationDto(content, notificationType, receiverId);
    }
}
