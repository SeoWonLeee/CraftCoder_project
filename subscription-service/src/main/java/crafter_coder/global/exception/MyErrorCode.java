package crafter_coder.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MyErrorCode {
    // 아래는 예시이니 필요하다면 변경 혹은 삭제해도 괜찮습니다.
    // User
     USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 유저입니다."),

    //Image
    NOT_GENERATE_FIRE_AI_IMAGE(HttpStatus.BAD_REQUEST,"횃불이 이미지가 생성 중 입니다."),

    // Queue
    QUEUE_SYSTEM_BUSY(HttpStatus.SERVICE_UNAVAILABLE, "시스템이 바쁩니다. 잠시 후 다시 시도해주세요."),
    QUEUE_FULL(HttpStatus.SERVICE_UNAVAILABLE, "해당 강좌의 대기열이 가득 찼습니다. 나중에 다시 시도해주세요."),
    LOCK_ACQUISITION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "잠금 획득에 실패했습니다. 잠시 후 다시 시도해주세요."),
    ;




    private final HttpStatus status;
    private final String message;
}

