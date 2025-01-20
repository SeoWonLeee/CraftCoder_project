package crafter_coder.global.exception;

import crafter_coder.domain.program.exception.ProgramErrorCode;
import crafter_coder.domain.program.exception.ProgramException;
import crafter_coder.domain.subscription.exception.SubscriptionErrorCode;
import crafter_coder.domain.subscription.exception.SubscriptionException;
import crafter_coder.openFeign.exception.RestApiException;
import crafter_coder.openFeign.dto.RestApiErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<RestApiErrorDto> handleRestApiException(RestApiException ex){
        log.error("예외 발생 msg:{}",ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(RestApiErrorDto.of(ex.getUrl(), ex.getMessage()));
    }

    @ExceptionHandler(ProgramException.class)
    public ResponseEntity<ProgramErrorCode> handleProgramException(ProgramException ex){
        log.error("예외 발생 msg:{}",ex.getErrorCode().getMessage());
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getErrorCode());
    }

    @ExceptionHandler(SubscriptionException.class)
    public ResponseEntity<SubscriptionErrorCode> handleSubscriptionException(SubscriptionException ex){
        log.error("예외 발생 msg:{}",ex.getErrorCode().getMessage());
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ex.getErrorCode());
    }

}

