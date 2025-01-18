package crafter_coder.domain.notification.emitter.util;

public class IdGenerator {
    /*
        {userId}_{timestamp} 형태의 Id 생성
        lastEventId와 캐시에 저장해둔 이벤트들의 id를 timestamp 기준으로 비교하여 클라이언트가 받지 못한 이벤트들을 재전송할 수 있음
    */
    public static String makeTimeIncludeId(Long id) {
        return id + "_" + System.currentTimeMillis();
    }

    public static Long extractUserId(String combinedId) {
        int underscoreIndex = combinedId.indexOf('_');
        if (underscoreIndex != -1) {
            return Long.valueOf(combinedId.substring(0, underscoreIndex));
        } else {
            // 언더스코어가 없을 경우 전체 문자열 반환 또는 예외 처리
            return Long.valueOf(combinedId);
        }
    }
}
