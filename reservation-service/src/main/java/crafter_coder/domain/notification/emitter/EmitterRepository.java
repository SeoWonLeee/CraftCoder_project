package crafter_coder.domain.notification.emitter;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepository {
     /*
         sseEmitter를 저장하는 Map, key는 receiverId + "_" + System.currentTimeMillis()
         여러 스레드가 동시에 접근할 수 있으므로 ConcurrentHashMap 사용
     */
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
     /*
         클라이언트에게 발송한 이벤트를 저장하는 Map, key는 receiverId + "_" + System.currentTimeMillis()
         이벤트 발송이 실패하여 클라이언트가 재연결을 시도할 때 못받은 이벤트를 재발송하기 위해 사용
         문제는 메모리에만 저장하기 때문에 서버가 재시작되면 데이터가 사라짐, 이를 해결하기 위해선 DB나 Redis에 저장해야 함
     */
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    public void save(String emitterId, SseEmitter emitter) {
        emitters.put(emitterId, emitter);
    }

    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    public Map<String, SseEmitter> findAllEmittersByUserId(String userId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, Object> findAllEventCacheByUserId(String userId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void deleteById(String emitterId) {
        emitters.remove(emitterId);
    }

    public void deleteAllEmittersByUserId(String receiverId) {
        List<String> emitterIds = emitters.keySet().stream()
                .filter(key -> key.startsWith(receiverId))
                .toList();
        emitterIds.forEach(emitters::remove);
    }

    public void deleteAllEventCacheByUserId(String userId) {
        List<String> eventIds = eventCache.keySet().stream()
                .filter(key -> key.startsWith(userId))
                .toList();

        eventIds.forEach(eventCache::remove);
    }


}
