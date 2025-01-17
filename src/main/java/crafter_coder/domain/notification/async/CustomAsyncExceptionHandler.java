package crafter_coder.domain.notification.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/*
* 알림 발송 async 메소드가 void 반환하므로 예외처리 핸들러 추가 필요
* */
@Slf4j
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("클라이언트에게 알림 전송 중 예외 발생: {}", ex.getMessage());
        log.error("메소드 이름: {}", method.getName());
        for (Object param : params) {
            log.error("파라미터 값: {}", param);
        }
    }
}
