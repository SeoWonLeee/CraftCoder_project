package crafter_coder.domain.notification.service;

import crafter_coder.domain.notification.NotificationDto;
import crafter_coder.domain.notification.NotificationType;
import crafter_coder.domain.notification.emitter.EmitterRepository;
import crafter_coder.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final EmitterRepository emitterRepository;
    private final Long timeoutMillis = 600_000L;

    public SseEmitter subscribe(Long userId, String lastEventId) {
        String emitterId = makeTimeIncludeId(userId);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeoutMillis));
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId)); // SSE 연결 종료 시
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId)); // SSE 연결 시간 초과 시
        // 이벤트 전송 실패 시 emitter 제거, 클라이언트에서 자동으로 재연결 시도
        emitter.onError(e -> emitterRepository.deleteById(emitterId));

        String eventId = makeTimeIncludeId(userId);
        sendNotification(emitter, eventId,
                emitterId, "이벤트 stream 생성. [userId=%d]".formatted(userId));

        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, emitter);
        }

        return emitter;
    }

    public void send(Long userId, NotificationType notificationType, String content) {
        String eventId = makeTimeIncludeId(userId);
        NotificationDto notification = NotificationDto.of(content, notificationType, userId);
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmittersByUserId(userId.toString());
        emitters.forEach(
                (id, emitter) -> {
                    emitterRepository.saveEventCache(id, notification);
                    sendNotification(emitter, eventId, id,
                            notification);
                }
        );
    }

    /*
        {userId}_{timestamp} 형태의 Id 생성
        lastEventId와 캐시에 저장해둔 이벤트들의 id를 timestamp 기준으로 비교하여 클라이언트가 받지 못한 이벤트들을 재전송할 수 있음
    */
    private String makeTimeIncludeId(Long id) {
        return id + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository
                .findAllEventCacheByUserId(String.valueOf(userId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }
}
