package crafter_coder.global.domain.redis;

import crafter_coder.global.exception.MyErrorCode;
import crafter_coder.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisQueueFacade {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedissonClient redissonClient;

    private static final String WAITING_QUEUE_KEY = "queue:waiting:";
    private static final String ACTIVE_QUEUE_KEY = "queue:active:";
    private static final int ACTIVE_QUEUE_TTL = 60; // 활성 큐 TTL (초)
    private static final int MAX_WAITING_QUEUE_SIZE = 2000; // 최대 대기 큐 크기
    /**
     * 대기 큐 생성
     */
    public AddQueueResponse addToWaitingQueue(Long courseId, String userId) {
        String waitingQueueKey = WAITING_QUEUE_KEY + courseId;

        RLock lock = redissonClient.getLock("lock:course:" + courseId);
        try {
            boolean isLockAcquired = lock.tryLock(5, TimeUnit.SECONDS);
            if (!isLockAcquired) {
                throw new MyException(MyErrorCode.QUEUE_SYSTEM_BUSY);
            }

            // 대기 큐 크기 확인
            Long queueSize = redisTemplate.opsForZSet().size(waitingQueueKey);
            if (queueSize != null && queueSize >= MAX_WAITING_QUEUE_SIZE) {
                throw new MyException(MyErrorCode.QUEUE_FULL);
            }

            // 대기 큐에 추가
            long timestamp = System.currentTimeMillis();
            redisTemplate.opsForZSet().add(waitingQueueKey, userId, timestamp);
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
    public void processWaitingQueue(Long courseId, int limit) {
        String waitingQueueKey = WAITING_QUEUE_KEY + courseId;
        String activeQueueKey = ACTIVE_QUEUE_KEY + courseId;

        Set<String> users = redisTemplate.opsForZSet().range(waitingQueueKey, 0, limit - 1);

        if (users != null && !users.isEmpty()) {
            for (String userId : users) {
                // 활성 큐에 추가
                redisTemplate.opsForHash().put(activeQueueKey, userId, "결제 대기중");
                redisTemplate.expire(activeQueueKey, ACTIVE_QUEUE_TTL, TimeUnit.SECONDS);

                // 대기 큐에서 제거
                redisTemplate.opsForZSet().remove(waitingQueueKey, userId);
            }
        }
    }

    /**
     * 활성 큐 검증 및 TTL 연장
     */
    public boolean verifyAndExtendActiveQueue(Long courseId, String userId) {
        String activeQueueKey = ACTIVE_QUEUE_KEY + courseId;

        boolean isActive = redisTemplate.opsForHash().hasKey(activeQueueKey, userId);
        if (isActive) {
            redisTemplate.expire(activeQueueKey, ACTIVE_QUEUE_TTL, TimeUnit.SECONDS);
        }
        return isActive;
    }

    /**
     * 결제 완료 처리
     */
    public void completePayment(Long courseId, String userId) {
        String activeQueueKey = ACTIVE_QUEUE_KEY + courseId;

        // 활성 큐에서 제거
        redisTemplate.opsForHash().delete(activeQueueKey, userId);

        // RDB에 결과 저장
        saveToDatabase(courseId, userId);
    }

    private void saveToDatabase(Long courseId, String userId) {
        // RDB 저장 로직 구현
        System.out.println("Saved courseId: " + courseId + ", userId: " + userId + " to DB.");
    }

    /**
     *  스케쥴러를 통한 로직 처리
     * @param courseId
     */
    @Scheduled(fixedRate = 1000) // 1초마다 실행
    public void processActiveQueue(Long courseId) {
        String activeQueueKey = ACTIVE_QUEUE_KEY + courseId;

        // 활성 큐 만료 상태 확인
        Map<Object, Object> activeQueue = redisTemplate.opsForHash().entries(activeQueueKey);

        activeQueue.forEach((userId, status) -> {
            if (!"결제 완료".equals(status)) {
                redisTemplate.opsForHash().delete(activeQueueKey, userId);
                System.out.println("User " + userId + " removed from active queue.");
            }
        });
    }

}

