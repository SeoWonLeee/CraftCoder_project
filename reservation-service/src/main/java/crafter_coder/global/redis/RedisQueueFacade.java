//package crafter_coder.global.redis;
//
//import crafter_coder.domain.course.model.Course;
//import crafter_coder.domain.course.repository.CourseRepository;
//import crafter_coder.domain.user.model.User;
//import crafter_coder.domain.user.repository.UserRepository;
//import crafter_coder.domain.user_course.model.UserCourse;
//import crafter_coder.domain.user_course.repository.UserCourseRepository;
//import crafter_coder.global.exception.MyErrorCode;
//import crafter_coder.global.exception.MyException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ZSetOperations;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
//@Service @Slf4j
//@RequiredArgsConstructor
//public class RedisQueueFacade {
//
//    private final RedisTemplate<String, String> redisTemplate;
//    private final RedissonClient redissonClient;
//    private final UserCourseRepository userCourseRepository;
//    private final CourseRepository courseRepository;
//    private final UserRepository userRepository;
//
//
//    private static final String WAITING_QUEUE_KEY = "queue:waiting:";
//    private static final String ACTIVE_QUEUE_KEY = "queue:active:";
//    private static final String COURSE_IDS_SET = "course:ids"; // 모든 강좌 ID 저장용 키
//    private static final int ACTIVE_QUEUE_TTL = 30; // 활성 큐 TTL (초)
//    private static final int MAX_ACTIVE_QUEUE_SIZE = 60; // 최대 대기 큐 크기
//    private static final int MAX_WAITING_QUEUE_SIZE = 15; // 최대 대기 큐 크기
//
//    /**
//     * 강좌 신청 로직
//     */
//
//    public AddQueueResponse addApplyToWaitingQueue(Long courseId, String userId) {
//        /**
//         * 대기큐에 값 넣기(Sorted Set 자료구조 사용)
//         */
//        String waitingQueueKey = WAITING_QUEUE_KEY + courseId; // 대기큐에 들어갈 (강좌 + 사용자) 키
//        Long queueSize = redisTemplate.opsForZSet().size(waitingQueueKey); // 대기 큐 크기 확인
//
//        log.info("courseId queueSize = {}", queueSize);
//
//        if (queueSize == null || queueSize >= MAX_WAITING_QUEUE_SIZE) { // 해당 강좌 신청에 대한 대기큐 지정 인원이 넘으면 취소 처리
//            throw new MyException(MyErrorCode.QUEUE_FULL);
//        }
//
//        RLock lock = redissonClient.getLock("lock:course:" + courseId); // 추후 동시성 문제를 고려해서 분산 락 사용
//        try {
//            // 락 확인
//            boolean isLockAcquired = lock.tryLock(5, TimeUnit.SECONDS);
//            if (!isLockAcquired) {
//                throw new MyException(MyErrorCode.QUEUE_SYSTEM_BUSY);
//            }
//            // 대기 큐에 추가
//            long timestamp = System.currentTimeMillis();
//            redisTemplate.opsForZSet().add(waitingQueueKey, userId, timestamp);
//            redisTemplate.opsForSet().add(COURSE_IDS_SET, String.valueOf(courseId)); // manageAllQueues 메서드에서 강좌 ID 별로 관리하기 위함
//
//            log.info("대기열에 추가 - {}, {} ({}초)", waitingQueueKey,userId, timestamp);
//
//            return AddQueueResponse.of("Added to the waiting queue.");
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            throw new MyException(MyErrorCode.LOCK_ACQUISITION_FAILED);
//        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }
//
//
//    private void saveToDatabase(Long courseId, String userId) { // 결제 완료 후, db 저장 로직
//        // RDB 저장 로직 구현
//        User user = userRepository.findById(Long.parseLong(userId))
//                .orElseThrow(() -> new MyException(MyErrorCode.COURSE_NOT_FOUND));
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new MyException(MyErrorCode.COURSE_NOT_FOUND));
//
//        // 2. 중복 확인: 이미 저장된 UserCourse가 있는지 확인
//        boolean exists = userCourseRepository.existsByCourseAndUser(course, user);
//        if (exists) {
//            log.info("UserCourse는 이미 존재합니다.(중복 신청) courseId: {}", userId, courseId);
//            return; // 중복된 경우 저장하지 않음
//        }
//
//        // 3. UserCourse 엔티티 생성
//        UserCourse userCourse = UserCourse.of(user, course);
//        userCourseRepository.save(userCourse);
//        log.info("Saved courseId: " + "{}" + ", userId: " + "{}" + " to DB.", courseId, userId);
//    }
//
//
//    /**
//     * 스케줄러 - 모든 강좌에 대해 대기열과 활성 큐 관리
//     */
//    @Scheduled(fixedRate = 10000) // 1초마다 실행 - 수동 검증을 위해 10초로 설정
//    public void manageAllQueues() {
//
//        log.info("스케줄러 동기화 확인 : {}초", System.currentTimeMillis());
//        Set<String> courseIds = redisTemplate.opsForSet().members(COURSE_IDS_SET);
//        if (courseIds == null || courseIds.isEmpty()) {
//            log.info("강좌 신청 정보가 없습니다.");
//            return;
//        }
//
//        for (String courseId : courseIds) {
//            log.info("courseId: {}", courseId);
//            manageWaitQueues(Long.parseLong(courseId));
//            manageActiveQueues(Long.parseLong(courseId));
//        }
//    }
//
//    private void manageActiveQueues(Long courseId) {
//        String activeQueueKey = ACTIVE_QUEUE_KEY + courseId;
//        // 1. 활성 큐 상태 확인
//        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.opsForZSet().rangeWithScores(activeQueueKey, 0, MAX_ACTIVE_QUEUE_SIZE);
//        if (typedTuples == null || typedTuples.isEmpty()) {
//            log.info("활성 큐가 비어 있습니다. courseId: {}", courseId);
//            return;
//        }
//        for (ZSetOperations.TypedTuple<String> entry : typedTuples) {
//            String userId = entry.getValue();
//            Double entryTime = entry.getScore(); // 활성 큐의 점수는 사용자의 활성화 시간
//
//            log.info("처리 중 - courseId: {}, userId: {}, 활성화 시간: {}", courseId, userId, entryTime);
//            // 강좌 결제 확인 로직이 없기에 활성큐에 오면 바로 저장하게 처리 / 결제 확인 로직 필요
//            saveToDatabase(courseId, userId); // 처리 로직: RDB 저장 등
//            redisTemplate.opsForZSet().remove(activeQueueKey, userId);// 활성 큐에서 사용자 제거
//
//            log.info("활성 큐에서 제거 - courseId: {}, userId: {}", courseId, userId);
//        }
//
//    }
//
//    private void manageWaitQueues(Long courseId) {
//        String waitingQueueKey = WAITING_QUEUE_KEY + courseId;
//        String activeQueueKey = ACTIVE_QUEUE_KEY + courseId;
//
//        // 1. 활성 큐 상태 확인
//        Set<ZSetOperations.TypedTuple<String>> activeQueue = redisTemplate.opsForZSet().rangeWithScores(activeQueueKey, 0, -1);
//        log.info("{} 강좌 신청 대기열 확인 중", courseId);
//        if(activeQueue == null){
//            log.info("해당 강좌 아이디 {} 대기열은 없습니다.", courseId);
//            return;
//        }
//
//        // 2. 활성큐에서 만료된 사용자(시간 초과) 제거 / 결제 취소도 고려를 해야할까?
//        int removedActiveQueueSize = 0;
//        long currentTime = System.currentTimeMillis();
//        for (ZSetOperations.TypedTuple<String> entry : activeQueue) {
//            String userId = entry.getValue();
//            Double entryTime = entry.getScore(); // 활성 큐에 추가된 시간 (점수)
//
//            if (entryTime != null && (currentTime - entryTime) > ACTIVE_QUEUE_TTL * 1000) {
//                redisTemplate.opsForZSet().remove(activeQueueKey, userId); // 활성 큐에서 제거
//                removedActiveQueueSize++;
//                log.info("activeKey = {}, User = {}, removed due to timeout. 알림 발송", activeQueueKey, userId); //시간 초과로 인해 신청 실패 알림 전송 로직(미구현)
//            }
//        }
//
//        // 3. 활성 큐 빈자리 수 구하기
//        int activeQueueSize = activeQueue.size();
//        int availableSlots = MAX_ACTIVE_QUEUE_SIZE - (activeQueueSize - removedActiveQueueSize);
//        log.info("활성큐 빈자리의 수는 {} 입니다.", availableSlots);
//        if (availableSlots <= 0) {  // 활성 큐에 빈 자리가 있을 경우
//            return;
//        }
//        // 4. 활성 큐 빈자리 수만큼 대기큐 값 제거 + 활성큐 추가
//        Set<ZSetOperations.TypedTuple<String>> users = redisTemplate.opsForZSet().rangeWithScores(waitingQueueKey, 0, availableSlots - 1); // 대기 큐에서 가져오기
//        if (users != null && !users.isEmpty()) {
//            for (ZSetOperations.TypedTuple<String> entry : users) {
//                String userId = entry.getValue();
//                long timestamp = System.currentTimeMillis();
//
//                redisTemplate.opsForZSet().add(activeQueueKey, userId, timestamp); // 활성 큐에 추가 (점수는 현재 시간)
//                redisTemplate.opsForZSet().remove(waitingQueueKey, userId); // 대기 큐에서 제거
//
//                log.info("waitingKey = {}, User = {}, moved to active queue.", waitingQueueKey, userId);
//            }
//        }
//
//    }
//
//
//}

package crafter_coder.global.redis;

import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.course.repository.CourseRepository;
import crafter_coder.domain.user.model.User;
import crafter_coder.domain.user.repository.UserRepository;
import crafter_coder.domain.user_course.model.EnrollmentStatus;
import crafter_coder.domain.user_course.model.UserCourse;
import crafter_coder.domain.user_course.repository.UserCourseRepository;
import crafter_coder.global.exception.MyErrorCode;
import crafter_coder.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * RedisQueueFacade:
 * Redis를 사용하여 강좌 신청 대기 큐와 활성 큐를 관리하고,
 * 결제 완료/취소, TTL 초과 등의 로직을 처리하는 클래스.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RedisQueueFacade {

    // Redis와 데이터를 주고받기 위한 객체
    private final RedisTemplate<String, String> redisTemplate;

    // 분산 락 처리를 위한 Redisson 클라이언트
    private final RedissonClient redissonClient;

    // 데이터베이스와 연결된 Repository들
    private final UserCourseRepository userCourseRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    // Redis 키와 관련된 상수 정의
    private static final String WAITING_QUEUE_KEY = "queue:waiting:"; // 대기 큐의 Redis 키 Prefix
    private static final String ACTIVE_QUEUE_KEY = "queue:active:";  // 활성 큐의 Redis 키 Prefix
    private static final String COURSE_IDS_SET = "course:ids";       // 모든 강좌 ID를 저장하는 Redis 키

    // 활성 큐와 대기 큐의 크기 및 TTL 설정
    private static final int ACTIVE_QUEUE_TTL = 86400; // 활성 큐 TTL (초) - 24시간
    private static final int MAX_ACTIVE_QUEUE_SIZE = 30; // 최대 활성 큐 크기
    private static final int MAX_WAITING_QUEUE_SIZE = 15; // 최대 대기 큐 크기

    /**
     * 강좌 신청 로직: 대기 큐에 사용자 추가
     *
     * @param courseId 강좌 ID
     * @param userId   사용자 ID
     * @return AddQueueResponse 대기 큐 추가 응답 객체
     */
    public AddQueueResponse addApplyToWaitingQueue(@NonNull Long courseId, @NonNull String userId) {
        // Redis에서 대기 큐 키 생성
        String waitingQueueKey = WAITING_QUEUE_KEY + courseId;

        // 대기 큐의 현재 크기를 확인
        Long queueSize = redisTemplate.opsForZSet().size(waitingQueueKey);
        if (queueSize == null || queueSize >= MAX_WAITING_QUEUE_SIZE) {
            throw new MyException(MyErrorCode.QUEUE_FULL); // 대기 큐가 가득 찬 경우 예외 발생
        }

        // 동시성 문제를 방지하기 위해 분산 락을 사용
        executeWithLock("lock:course:" + courseId, () -> {
            long timestamp = System.currentTimeMillis();
            redisTemplate.opsForZSet().add(waitingQueueKey, userId, timestamp); // 대기 큐에 사용자 추가
            redisTemplate.opsForSet().add(COURSE_IDS_SET, String.valueOf(courseId)); // 강좌 ID를 관리용 Set에 추가
            log.info("대기열에 추가 - courseId: {}, userId: {}", courseId, userId);
        });

        return AddQueueResponse.of("Added to the waiting queue.");
    }

    /**
     * 결제 완료 처리: 활성 큐에서 사용자를 제거하고 DB 상태를 PAYED로 업데이트
     *
     * @param courseId 강좌 ID
     * @param userId   사용자 ID
     */
    public void completePayment(@NonNull Long courseId, @NonNull String userId) {
        String activeQueueKey = ACTIVE_QUEUE_KEY + courseId;

        executeWithLock("lock:course:" + courseId, () -> {
            boolean removed = redisTemplate.opsForZSet().remove(activeQueueKey, userId) != null;
            if (!removed) {
                throw new MyException(MyErrorCode.USER_NOT_IN_ACTIVE_QUEUE); // 활성 큐에 사용자가 없는 경우 예외 발생
            }
            updateUserCourseStatus(courseId, userId, EnrollmentStatus.PAYED); // 상태를 PAYED로 업데이트
            log.info("결제 완료 처리 - courseId: {}, userId: {}", courseId, userId);
        });
    }

    /**
     * 결제 취소 처리: 상태를 CANCELED로 업데이트
     *
     * @param courseId 강좌 ID
     * @param userId   사용자 ID
     */
    public void cancelPayment(@NonNull Long courseId, @NonNull String userId) {
        executeWithLock("lock:course:" + courseId, () -> {
            updateUserCourseStatus(courseId, userId, EnrollmentStatus.CANCELED); // 상태를 CANCELED로 업데이트
            log.info("결제 취소 처리 - courseId: {}, userId: {}", courseId, userId);
        });
    }

    /**
     * 스케줄러: 모든 큐 관리
     * - TTL 초과된 사용자 제거
     * - 활성 큐의 빈자리를 대기 큐에서 채움
     */
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void manageAllQueues() {
        Set<String> courseIds = redisTemplate.opsForSet().members(COURSE_IDS_SET);
        if (courseIds == null || courseIds.isEmpty()) {
            log.info("관리할 큐가 없습니다.");
            return;
        }

        for (String courseId : courseIds) {
            manageWaitQueues(Long.parseLong(courseId)); // 대기 큐 관리
            manageActiveQueues(Long.parseLong(courseId)); // 활성 큐 관리
        }
    }

    /**
     * 대기 큐 관리: 활성 큐의 빈자리를 대기 큐에서 채움
     *
     * @param courseId 강좌 ID
     */
    private void manageWaitQueues(@NonNull Long courseId) {
        String waitingQueueKey = WAITING_QUEUE_KEY + courseId;
        String activeQueueKey = ACTIVE_QUEUE_KEY + courseId;

        // 활성 큐 크기 확인
        Long activeQueueSize = redisTemplate.opsForZSet().size(activeQueueKey);
        if (activeQueueSize == null) activeQueueSize = 0L;

        // 활성 큐의 빈자리 계산
        int availableSlots = MAX_ACTIVE_QUEUE_SIZE - activeQueueSize.intValue();
        if (availableSlots <= 0) return;

        // 대기 큐에서 활성 큐로 사용자 이동
        Set<ZSetOperations.TypedTuple<String>> usersToMove = redisTemplate.opsForZSet()
                .rangeWithScores(waitingQueueKey, 0, availableSlots - 1);
        if (usersToMove != null) {
            for (ZSetOperations.TypedTuple<String> user : usersToMove) {
                String userId = user.getValue();

                // userId가 null인 경우 처리
                if (userId == null) {
                    log.warn("대기 큐에서 null userId를 발견했습니다. courseId: {}", courseId);
                    continue;
                }

                // 활성 큐에 추가
                redisTemplate.opsForZSet().add(activeQueueKey, userId, System.currentTimeMillis());
                redisTemplate.opsForZSet().remove(waitingQueueKey, userId);
                log.info("대기 큐 -> 활성 큐 이동 - courseId: {}, userId: {}", courseId, userId);
            }
        }
    }

    /**
     * 활성 큐 관리: TTL 초과된 사용자 제거
     *
     * @param courseId 강좌 ID
     */
    private void manageActiveQueues(@NonNull Long courseId) {
        String activeQueueKey = ACTIVE_QUEUE_KEY + courseId;

        // TTL 초과된 사용자 검색
        long currentTime = System.currentTimeMillis();
        Set<String> expiredUsers = redisTemplate.opsForZSet()
                .rangeByScore(activeQueueKey, 0, currentTime - ACTIVE_QUEUE_TTL * 1000);
        if (expiredUsers != null) {
            for (String userId : expiredUsers) {
                redisTemplate.opsForZSet().remove(activeQueueKey, userId);
                updateUserCourseStatus(courseId, userId, EnrollmentStatus.CANCELED); // 상태를 CANCELED로 업데이트
                log.info("TTL 초과 사용자 제거 - courseId: {}, userId: {}", courseId, userId);
            }
        }
    }

    /**
     * UserCourse 상태 업데이트
     *
     * @param courseId 강좌 ID
     * @param userId   사용자 ID
     * @param status   새로운 상태 (PAYED, CANCELED 등)
     */
    private void updateUserCourseStatus(@NonNull Long courseId, @NonNull String userId, EnrollmentStatus status) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new MyException(MyErrorCode.USER_NOT_FOUND));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new MyException(MyErrorCode.COURSE_NOT_FOUND));

        UserCourse userCourse = userCourseRepository.findByCourseAndUser(course, user)
                .orElseThrow(() -> new MyException(MyErrorCode.USER_NOT_IN_COURSE));

        userCourse.updateStatus(status);
        userCourseRepository.save(userCourse);

        log.info("UserCourse 상태 업데이트 - courseId: {}, userId: {}, status: {}", courseId, userId, status);
    }

    /**
     * 분산 락 처리 유틸리티 메서드
     *
     * @param lockKey 락 키
     * @param task    수행할 작업
     */
    private void executeWithLock(String lockKey, Runnable task) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (!lock.tryLock(5, TimeUnit.SECONDS)) {
                throw new MyException(MyErrorCode.QUEUE_SYSTEM_BUSY); // 락을 얻지 못한 경우 예외 발생
            }
            task.run(); // 작업 수행
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MyException(MyErrorCode.LOCK_ACQUISITION_FAILED); // 인터럽트 발생 시 예외 처리
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock(); // 락 해제
            }
        }
    }
}
