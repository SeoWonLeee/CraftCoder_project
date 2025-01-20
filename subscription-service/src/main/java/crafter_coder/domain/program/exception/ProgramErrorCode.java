package crafter_coder.domain.program.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProgramErrorCode {
    PROGRAM_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "프로그램 이름이 이미 존재합니다."),
    PROGRAM_NOT_FOUND(HttpStatus.NOT_FOUND, "프로그램을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
