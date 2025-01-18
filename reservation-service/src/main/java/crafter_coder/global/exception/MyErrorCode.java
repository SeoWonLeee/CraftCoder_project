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
    NOT_GENERATE_FIRE_AI_IMAGE(HttpStatus.BAD_REQUEST,"횃불이 이미지가 생성 중 입니다.");


    private final HttpStatus status;
    private final String message;
}

