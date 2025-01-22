package crafter_coder.domain.notification.emitter;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = EmitterRepository.class)
class EmitterRepositoryTest {

    @Autowired
    private EmitterRepository emitterRepository;

    @AfterEach
    public void after() {
        emitterRepository.deleteAllEmittersByUserId("1");
        emitterRepository.deleteAllEventCacheByUserId("1");
    }


    @Test
    @DisplayName("emitters concurrentHashMap에 sseEmitter를 저장한다.")
    void save() {
        // given
        SseEmitter sseEmitter = new SseEmitter();
        emitterRepository.save("1_1234", sseEmitter);

        // when
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmittersByUserId("1");

        // then
        assertEquals(1, emitters.size());
    }

    @Test
    @DisplayName("eventCache concurrentHashMap에 event를 저장한다.")
    void saveEventCache() {
        // given
        emitterRepository.saveEventCache("1_1234", "event");

        // when
        Map<String, Object> eventCache = emitterRepository.findAllEventCacheByUserId("1");

        // then
        assertEquals(1, eventCache.size());
    }

    @Test
    @DisplayName("emitters에 저장된 sseEmitter들을 반환한다.")
    void findAllEmittersByUserId() {
        // given
        emitterRepository.save("1_12", new SseEmitter());
        emitterRepository.save("1_34", new SseEmitter());
        emitterRepository.save("1_56", new SseEmitter());

        // when
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmittersByUserId("1");

        // then
        assertEquals(3, emitters.size());
    }

    @Test
    @DisplayName("eventCache에 저장된 event들을 반환한다.")
    void findAllEventCacheByUserId() {
        // given
        emitterRepository.saveEventCache("1_12", "event1");
        emitterRepository.saveEventCache("1_34", "event2");
        emitterRepository.saveEventCache("1_56", "event3");

        // when
        Map<String, Object> eventCache = emitterRepository.findAllEventCacheByUserId("1");

        // then
        assertEquals(3, eventCache.size());
    }

    @Test
    @DisplayName("해당 Id의 sseEmitter를 emitters에서 삭제한다.")
    void deleteById() {
        // given
        emitterRepository.save("1_12", new SseEmitter());
        emitterRepository.save("1_34", new SseEmitter());

        // when
        emitterRepository.deleteById("1_12");

        // then
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmittersByUserId("1");
        assertEquals(1, emitters.size());
    }

    @Test
    @DisplayName("유저 ID에 해당하는 모든 sseEmitter를 emitters에서 삭제한다.")
    void deleteAllEmittersByUserId() {
        // given
        emitterRepository.save("1_12", new SseEmitter());
        emitterRepository.save("1_34", new SseEmitter());
        emitterRepository.save("1_56", new SseEmitter());

        // when
        emitterRepository.deleteAllEmittersByUserId("1");

        // then
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmittersByUserId("1");
        assertEquals(0, emitters.size());
    }

    @Test
    @DisplayName("유저 ID에 해당하는 모든 event를 eventCache에서 삭제한다.")
    void deleteAllEventCacheByUserId() {
        // given
        emitterRepository.saveEventCache("1_12", "event1");
        emitterRepository.saveEventCache("1_34", "event2");
        emitterRepository.saveEventCache("1_56", "event3");

        // when
        emitterRepository.deleteAllEventCacheByUserId("1");

        // then
        Map<String, Object> eventCache = emitterRepository.findAllEventCacheByUserId("1");
        assertEquals(0, eventCache.size());
    }

    // TODO: 이미 있는 emitterId로 sseEmitter를 저장할 경우, 기존의 sseEmitter를 덮어쓴다.
    // TODO: 이미 있는 eventCacheId로 event를 저장할 경우, 기존의 event를 덮어쓴다.
    // TODO: 존재하지 않는 emitterId로 sseEmitter를 삭제할 경우, 아무런 동작을 하지 않는다.
    // TODO: 존재하지 않는 userId로 sseEmitter를 삭제할 경우, 아무런 동작을 하지 않는다.
    // TODO: 존재하지 않는 userId로 event를 삭제할 경우, 아무런 동작을 하지 않는다.
}