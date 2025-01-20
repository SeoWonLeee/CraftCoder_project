package crafter_coder.global.redis;

import crafter_coder.global.exception.MyErrorCode;
import crafter_coder.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
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
    private static final int ACTIVE_QUEUE_TTL = 30; // 활성 큐 TTL (초)
    private static final int MAX_ACTIVE_QUEUE_SIZE = 60; // 최대 대기 큐 크기
    private static final int MAX_WAITING_QUEUE_SIZE = 15; // 최대 대기 큐 크기

    /**
     * 강좌 신청 로직
     */
    public AddQueueResponse addApplyToWaitingQueue(Long courseId, String userId) {
        String waitingQueueKey = WAITING_QUEUE_KEY + courseId; // 대기큐에 들어갈 (강좌 + 사용자) 키
        Long queueSize = redisTemplate.opsForZSet().size(waitingQueueKey); // 대기 큐 크기 확인

        log.info("courseId queueSize = {}", queueSize);

        if (queueSize == null || queueSize >= MAX_WAITING_QUEUE_SIZE) {
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
            redisTemplate.opsForSet().add(COURSE_IDS_SET, String.valueOf(courseId));

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
    @Scheduled(fixedRate = 10000) // 1초마다 실행
    public void manageAllQueues() {

        log.info("스케줄러 동기화 확인 : {}초", System.currentTimeMillis());
        Set<String> courseIds = redisTemplate.opsForSet().members(COURSE_IDS_SET);
        if (courseIds == null || courseIds.isEmpty()) {
            log.info("강좌 신청 정보가 없습니다.");
        }

        for (String courseId : courseIds) {
            log.info("courseId: {}", courseId);
            manageWaitQueues(Long.parseLong(courseId));
            manageActiveQueues(Long.parseLong(courseId));
        }
    }

    private void manageActiveQueues(Long courseId) {
        String activeQueueKey = ACTIVE_QUEUE_KEY + courseId;
        // 1. 활성 큐 상태 확인
        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.opsForZSet().rangeWithScores(activeQueueKey, 0, MAX_ACTIVE_QUEUE_SIZE);
        if (typedTuples == null || typedTuples.isEmpty()) {
            log.info("활성 큐가 비어 있습니다. courseId: {}", courseId);
            return;
        }
        for (ZSetOperations.TypedTuple<String> entry : typedTuples) {
            String userId = entry.getValue();
            Double entryTime = entry.getScore(); // 활성 큐의 점수는 사용자의 활성화 시간

            log.info("처리 중 - courseId: {}, userId: {}, 활성화 시간: {}", courseId, userId, entryTime);

            saveToDatabase(courseId, userId); // 처리 로직: RDB 저장 등
            redisTemplate.opsForZSet().remove(activeQueueKey, userId);// 활성 큐에서 사용자 제거

            log.info("활성 큐에서 제거 - courseId: {}, userId: {}", courseId, userId);
        }

    }

    private void manageWaitQueues(Long courseId) {
        String waitingQueueKey = WAITING_QUEUE_KEY + courseId;
        String activeQueueKey = ACTIVE_QUEUE_KEY + courseId;

        // 1. 활성 큐 상태 확인
        Set<ZSetOperations.TypedTuple<String>> activeQueue = redisTemplate.opsForZSet().rangeWithScores(activeQueueKey, 0, -1);
        log.info("{} 강좌 신청 대기열 확인 중", courseId);
        if(activeQueue == null){
            log.info("해당 강좌 아이디 {} 대기열은 없습니다.", courseId);
            return;
        }

        // 2. 만료된 사용자 제거
        int removedActiveQueueSize = 0;
        long currentTime = System.currentTimeMillis();
        for (ZSetOperations.TypedTuple<String> entry : activeQueue) {
            String userId = entry.getValue();
            Double entryTime = entry.getScore(); // 활성 큐에 추가된 시간 (점수)

            if (entryTime != null && (currentTime - entryTime) > ACTIVE_QUEUE_TTL * 1000) {
                redisTemplate.opsForZSet().remove(activeQueueKey, userId); // 활성 큐에서 제거
                removedActiveQueueSize++;
                log.info("activeKey = {}, User = {}, removed due to timeout. 알림 발송", activeQueueKey, userId); //시간 초과로 인해 신청 실패 알림 전송 로직(미구현)
            }
        }

        // 3. 활성 큐 빈자리 수 구하기
        int activeQueueSize = activeQueue.size();
        int availableSlots = MAX_ACTIVE_QUEUE_SIZE - (activeQueueSize - removedActiveQueueSize);
        log.info("활성큐 빈자리의 수는 {} 입니다.", availableSlots);
        if (availableSlots <= 0) {  // 활성 큐에 빈 자리가 있을 경우
            return;
        }

        Set<ZSetOperations.TypedTuple<String>> users = redisTemplate.opsForZSet().rangeWithScores(waitingQueueKey, 0, availableSlots - 1); // 대기 큐에서 가져오기
        if (users != null && !users.isEmpty()) {
            for (ZSetOperations.TypedTuple<String> entry : users) {
                String userId = entry.getValue();
                long timestamp = System.currentTimeMillis();

                redisTemplate.opsForZSet().add(activeQueueKey, userId, timestamp); // 활성 큐에 추가 (점수는 현재 시간)
                redisTemplate.opsForZSet().remove(waitingQueueKey, userId); // 대기 큐에서 제거

                log.info("waitingKey = {}, User = {}, moved to active queue.", waitingQueueKey, userId);
            }
        }

    }


}

