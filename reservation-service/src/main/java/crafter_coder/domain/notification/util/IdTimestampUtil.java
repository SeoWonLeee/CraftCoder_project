package crafter_coder.domain.notification.util;

import org.springframework.stereotype.Component;

@Component
public class IdTimestampUtil {
    public String makeTimeIncludeId(Long id) {
        return id + "_" + System.currentTimeMillis();
    }

    public Long extractUserId(String combinedId) {
        int underscoreIndex = combinedId.indexOf('_');
        if (underscoreIndex != -1) {
            return Long.valueOf(combinedId.substring(0, underscoreIndex));
        } else {
            // 언더스코어가 없을 경우 전체 문자열 반환 또는 예외 처리
            return Long.valueOf(combinedId);
        }
    }
}
