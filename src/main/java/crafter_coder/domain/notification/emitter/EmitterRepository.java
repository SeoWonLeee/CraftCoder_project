package crafter_coder.domain.notification.emitter;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepository {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(String emitterId, SseEmitter emitter) {
        emitters.put(emitterId, emitter);
        return emitter;
    }

    public void deleteById(String emitterId) {
        emitters.remove(emitterId);
    }

    public void deleteAllByReceiverId(String receiverId) {
        List<String> emitterIdList = emitters.keySet().stream()
                .filter(key -> key.startsWith(receiverId))
                .toList();
        emitterIdList.forEach(emitters::remove);
    }

    public Map<String, SseEmitter> findAllEmittersByReceiverId(String receiverId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(receiverId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
