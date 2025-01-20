package crafter_coder.global.exception;

import crafter_coder.global.dto.ResponseDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<RestApiExceptionDto> MyException(RestApiException ex){
        log.error("예외 발생 msg:{}",ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(RestApiExceptionDto.of(ex.getUrl(), ex.getMessage()));
    }

}

