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

    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 강좌입니다."),
    INVALID_STATUS(HttpStatus.BAD_REQUEST, "잘못된 상태값입니다."),
    COURSE_CANNOT_BE_DELETED(HttpStatus.BAD_REQUEST, "수강생이 있어 강좌를 삭제할 수 없습니다."),

    INSTRUCTOR_ONLY(HttpStatus.FORBIDDEN, "강사만 요청할 수 있습니다."),
    INVALID_REQUEST_DATA(HttpStatus.BAD_REQUEST, "요청 데이터가 잘못되었습니다."),
    CANNOT_UPDATE_COURSE(HttpStatus.BAD_REQUEST, "강좌 상태를 변경할 수 없습니다."),
    CANNOT_DELETE_COURSE(HttpStatus.BAD_REQUEST, "강좌 삭제 요청을 처리할 수 없습니다."),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID로 조회된 강사 요청을 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String message;
}

