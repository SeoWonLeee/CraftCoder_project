package crafter_coder.global.exception;

import crafter_coder.domain.program.exception.ProgramException;
import crafter_coder.domain.subscription.exception.SubscriptionException;
import crafter_coder.global.dto.ResponseDto;
import crafter_coder.global.openFeign.exception.RestApiException;
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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<ResponseDto<Void>> handleRestApiException(RestApiException ex){
        log.error("RestApiException 발생 msg:{}",ex.getMessage());
        ResponseDto<Void> response = ResponseDto.error(ex.getUrl(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(ProgramException.class)
    public ResponseEntity<ResponseDto<String>> handleProgramException(ProgramException ex){
        log.error("ProgramException 발생 msg:{}",ex.getMyErrorCode().getMessage());
        ResponseDto<String> response = ResponseDto.error(ex.getMyErrorCode().getMessage());
        return ResponseEntity.status(ex.getMyErrorCode().getStatus()).body(response);
    }

    @ExceptionHandler(SubscriptionException.class)
    public ResponseEntity<ResponseDto<String>> handleSubscriptionException(SubscriptionException ex){
        log.error("SubscriptionException 발생 msg:{}",ex.getMyErrorCode().getMessage());
        ResponseDto<String> response = ResponseDto.error(ex.getMyErrorCode().getMessage());
        return ResponseEntity.status(ex.getMyErrorCode().getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String message = fieldError.getDefaultMessage();
        log.error("유효성 검사 예외 발생 msg:{}",message);
        ResponseDto<String> response = ResponseDto.error(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AesEncryptException.class)
    public ResponseEntity<ResponseDto<String>> handleAesEncryptException(AesEncryptException ex){
        String message = "AES 암호화/복호화 중 오류가 발생했습니다: " + ex.getMessage();
        log.error("AesEncryptException 발생 msg:{}",message);
        ResponseDto<String> response = ResponseDto.error(message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDto<String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
        String message = "요청한 파라미터의 타입이 올바르지 않습니다: " + ex.getMessage();
        log.error("MethodArgumentTypeMismatchException 발생 msg:{}",message);
        ResponseDto<String> response = ResponseDto.error(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDto<String>> handleConstraintViolationException(ConstraintViolationException ex){
        String message = "요청한 파라미터의 값이 올바르지 않습니다: " + ex.getMessage();
        log.error("ConstraintViolationException 발생 msg:{}",message);
        ResponseDto<String> response = ResponseDto.error(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = "요청한 JSON 데이터를 읽을 수 없습니다: " + ex.getMessage();
        ResponseDto<String> response = ResponseDto.error(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

