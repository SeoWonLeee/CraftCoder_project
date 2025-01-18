package crafter_coder.domain.notification.service;

import crafter_coder.domain.notification.NotificationDto;
import crafter_coder.domain.notification.NotificationType;
import crafter_coder.domain.notification.emitter.EmitterRepository;
import crafter_coder.domain.notification.emitter.util.IdGenerator;
import crafter_coder.domain.notification.exception.NotificationException;
import crafter_coder.domain.notification.exception.NotificationException.NotificationSendException;
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

    public SseEmitter subscribe(String emitterId, String lastEventId) {
        // makeTimeIncludeId가 현재 시간에 의존하기 때문에 순수 함수가 될수 없고 테스트하기 어렵게 만듦, 시간 관련 id를 인자로 주입받도록 변경
        SseEmitter emitter = new SseEmitter(timeoutMillis);
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId)); // SSE 연결 종료 시
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId)); // SSE 연결 시간 초과 시
        // 이벤트 전송 실패 시 emitter 제거, 클라이언트에서 자동으로 재연결 시도
        emitter.onError(e -> emitterRepository.deleteById(emitterId));

        emitterRepository.save(emitterId, emitter);

        Long userId = IdGenerator.extractUserId(emitterId);

        sendNotification(emitter, emitterId,
                emitterId, "이벤트 stream 생성. [userId=%d]".formatted(userId));

        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, emitter);
        }

        return emitter;
    }

    public void send(String eventId, NotificationType notificationType, String content) {
        Long userId = IdGenerator.extractUserId(eventId);
        NotificationDto notification = NotificationDto.of(content, notificationType, userId);
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmittersByUserId(userId.toString());
        emitters.forEach(
                (id, emitter) -> {
                    emitterRepository.saveEventCache(eventId, notification);
                    sendNotification(emitter, eventId, id,
                            notification);
                }
        );
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
            throw new NotificationSendException();
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
