package crafter_coder.openFeign.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestApiException extends RuntimeException{
    private final HttpStatus status;
    private final String url;

    public RestApiException(HttpStatus status, String url, String message){
        super(message);
        this.status = status;
        this.url = url;
    }

    public static class RestApiServerException extends RestApiException{
        public RestApiServerException(HttpStatus status, String url, String message){
            super(status, url, message);
        }
    }

    public static class RestApiClientException extends RestApiException{
        public RestApiClientException(HttpStatus status, String url, String message){
            super(status, url, message);
        }
    }
}

