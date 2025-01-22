package crafter_coder.domain.program.exception;

import crafter_coder.global.exception.MyErrorCode;
import lombok.Getter;

@Getter
public class ProgramException extends RuntimeException {
    private final MyErrorCode myErrorCode;

    public ProgramException(MyErrorCode myErrorCode) {
        super(myErrorCode.getMessage());
        this.myErrorCode = myErrorCode;
    }

    public static class ProgramAlreadyExistsException extends ProgramException {
        public ProgramAlreadyExistsException() {
            super(MyErrorCode.PROGRAM_ALREADY_EXISTS);
        }
    }

    public static class ProgramNotFoundException extends ProgramException {
        public ProgramNotFoundException() {
            super(MyErrorCode.PROGRAM_NOT_FOUND);
        }
    }
}
