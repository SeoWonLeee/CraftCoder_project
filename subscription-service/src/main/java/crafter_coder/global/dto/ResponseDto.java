package crafter_coder.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {
    private final String resultCode;
    private final String message;
    private final String url;
    private final T data;

    @Builder
    private ResponseDto(String resultCode, String message, String url, T data) {
        this.resultCode = resultCode;
        this.message = message;
        this.url = url;
        this.data = data;
    }

    public static <T> ResponseDto<T> success(T data) {
        return ResponseDto.<T>builder()
                .resultCode("SUCCESS")
                .data(data)
                .build();
    }

    public static <T> ResponseDto<T> error(String message) {
        return ResponseDto.<T>builder()
                .resultCode("ERROR")
                .message(message)
                .data(null)
                .build();
    }

    public static <T> ResponseDto<T> error(String url, String message) {
        return ResponseDto.<T>builder()
                .resultCode("ERROR")
                .message(message)
                .url(url)
                .data(null)
                .build();
    }

}