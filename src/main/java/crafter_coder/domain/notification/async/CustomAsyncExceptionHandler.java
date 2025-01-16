package crafter_coder.domain.notification.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("SSE 위한 스프링 이벤트 발행 중 예외 발생: {}", ex.getMessage());
        log.error("메소드 이름: {}", method.getName());
        for (Object param : params) {
            log.error("파라미터 값: {}", param);
        }
    }
}
