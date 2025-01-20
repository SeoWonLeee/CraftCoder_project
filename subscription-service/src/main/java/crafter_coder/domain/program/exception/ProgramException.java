package crafter_coder.domain.program.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ProgramException extends RuntimeException {
    private final ProgramErrorCode errorCode;

    public ProgramException(ProgramErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static class ProgramAlreadyExistsException extends ProgramException {
        public ProgramAlreadyExistsException() {
            super(ProgramErrorCode.PROGRAM_ALREADY_EXISTS);
        }
    }

    public static class ProgramNotFoundException extends ProgramException {
        public ProgramNotFoundException() {
            super(ProgramErrorCode.PROGRAM_NOT_FOUND);
        }
    }
}
