package crafter_coder.global.redis;

import crafter_coder.global.exception.MyErrorCode;
import crafter_coder.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service @Slf4j
@RequiredArgsConstructor
public class RedisQueueFacade {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedissonClient redissonClient;

    private static final String WAITING_QUEUE_KEY = "queue:waiting:";
    private static final String ACTIVE_QUEUE_KEY = "queue:active:";
    private static final String COURSE_IDS_SET = "course:ids"; // 모든 강좌 ID 저장용 키
    private static final int ACTIVE_QUEUE_TTL = 60; // 활성 큐 TTL (초)
    private static final int MAX_ACTIVE_QUEUE_SIZE = 100; // 최대 대기 큐 크기
    private static final int MAX_WAITING_QUEUE_SIZE = 2000; // 최대 대기 큐 크기

    /**
     * 대기 큐 생성
     */
    public AddQueueResponse addToWaitingQueue(Long courseId, String userId) {
        String waitingQueueKey = WAITING_QUEUE_KEY + courseId; // 대기큐에 들어갈 (강좌 + 사용자) 키
        Long queueSize = redisTemplate.opsForZSet().size(waitingQueueKey); // 대기 큐 크기 확인

        if (queueSize != null && queueSize >= MAX_WAITING_QUEUE_SIZE) {
            throw new MyException(MyErrorCode.QUEUE_FULL);
        }

        RLock lock = redissonClient.getLock("lock:course:" + courseId);
        try {
            // 락 확인
            boolean isLockAcquired = lock.tryLock(5, TimeUnit.SECONDS);
            if (!isLockAcquired) {
                throw new MyException(MyErrorCode.QUEUE_SYSTEM_BUSY);
            }
            // 대기 큐에 추가
            long timestamp = System.currentTimeMillis();
            redisTemplate.opsForZSet().add(waitingQueueKey, userId, timestamp);
            log.info("대기열에 추가 - {}, {} ({}초)", waitingQueueKey,userId, timestamp);

            return AddQueueResponse.of("Added to the waiting queue.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MyException(MyErrorCode.LOCK_ACQUISITION_FAILED);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 활성 큐 생성
     */
    // 활성 큐에서 사용자 추가
    private void addToActiveQueue(Long courseId, String userId) {
        long timestamp = System.currentTimeMillis();
        String activeQueueKey = ACTIVE_QUEUE_KEY + courseId;

        redisTemplate.opsForHash().put(activeQueueKey, userId, String.valueOf(timestamp));
        redisTemplate.opsForSet().add(COURSE_IDS_SET, String.valueOf(courseId));
        log.info("{} 활성큐에 추가 - '{}'님이 입장함. ({}초)", activeQueueKey, userId, timestamp);
    }

    /**
     * 결제 완료 처리
     */
    public void completePayment(Long courseId, String userId) {
        String activeQueueKey = ACTIVE_QUEUE_KEY + courseId;

        // RDB에 결과 저장
        saveToDatabase(courseId, userId); // 미구현

        // 활성 큐에서 제거
        redisTemplate.opsForHash().delete(activeQueueKey, userId);
    }

    private void saveToDatabase(Long courseId, String userId) {
        // RDB 저장 로직 구현
        log.info("Saved courseId: " + "{}" + ", userId: " + "{}" + " to DB.", courseId, userId);
    }


    /**
     * 스케줄러 - 모든 강좌에 대해 대기열과 활성 큐 관리
     */
    @Scheduled(fixedRate = 1000) // 1초마다 실행
    public void manageAllQueues() {

        log.info("스케줄러 동기화 확인 : {}초", System.currentTimeMillis());
        Set<String> courseIds = redisTemplate.opsForSet().members(COURSE_IDS_SET);
        if (courseIds != null && !courseIds.isEmpty()) {
            for (String courseId : courseIds) {
                try {
                    manageQueues(Long.parseLong(courseId));
                } catch (Exception e) {
                    log.error("manageQueues 메서드 실패, for courseId {}, e = {}", courseId, e.getMessage());
                }
            }
        }else{
            log.info("강좌 신청 정보가 없습니다.");
        }
    }

    private void manageQueues(Long courseId) {
        String waitingQueueKey = WAITING_QUEUE_KEY + courseId;
        String activeQueueKey = ACTIVE_QUEUE_KEY + courseId;

        // 1. 활성 큐 상태 확인
        Map<Object, Object> activeQueue = redisTemplate.opsForHash().entries(activeQueueKey);

        // 2. 만료된 사용자 제거
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<Object, Object> entry : activeQueue.entrySet()) {
            Object key = entry.getKey();
            Object entryTime = entry.getValue();
            long elapsedTime = currentTime - Long.parseLong((String) entryTime);

            if (elapsedTime > ACTIVE_QUEUE_TTL * 1000) {
                redisTemplate.opsForHash().delete(activeQueueKey, key); // 활성 큐에서 제거
                log.info("activeKey = {}, User = {}, removed due to timeout. 알림 발송",activeQueueKey, key); //시간 초과로 인해 신청 실패 알림 전송 로직(미구현)
            }
        }

        int availableSlots = MAX_ACTIVE_QUEUE_SIZE - activeQueue.size(); // 활성큐 빈자리 수 구하기

        if (availableSlots > 0) {  // 3. 대기 큐에서 활성 큐로 이동 (활성 큐에 빈 자리가 있을 경우)
            Set<String> users = redisTemplate.opsForZSet().range(waitingQueueKey, 0, availableSlots - 1); // 대기 큐에서 가져오기

            if (users != null && !users.isEmpty()) {
                for (String userId : users) {
                    addToActiveQueue(courseId, userId); // 활성 큐에 추가 (진입 시간 기록)
                    // 대기 큐에서 제거
                    redisTemplate.opsForZSet().remove(waitingQueueKey, userId);
                    log.info("waitingKey = {}, User = {}, removed due to timeout.", waitingQueueKey, userId);

                }
            }
        }


    }


}

