package crafter_coder.global.openFeign.error;

import crafter_coder.global.exception.RestApiException.RestApiClientException;
import crafter_coder.global.exception.RestApiException.RestApiServerException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        String responseBody = parseResponseBody(response);
        String requestUrl = response.request().url();
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());
        log.error("FeignException 발생 : url={}, status={}, responseBody={}", requestUrl, responseStatus, responseBody);

        if(responseStatus.is5xxServerError()) {
            // TODO: 네트워크 에러 같이 재시도가 필요한 경우 RetryableException을 던지도록 수정
            return new RestApiServerException(responseStatus, requestUrl, responseBody);
        } else if(responseStatus.is4xxClientError()) {
            return new RestApiClientException(responseStatus, requestUrl, responseBody);
        } else {
            return new RuntimeException("알 수 없는 에러가 발생했습니다.");
        }
    }

    /*
        Response의 body가 inputStream 형태로 들어오기 때문에
        InputStreamReader를 사용하여 바이트를 Stream 형태로 변환하고
        BufferedReader를 사용하여 Stream 문자열들을 개행 문자로 연결하여 하나의 문자열로 반환한다.
     */
    private String parseResponseBody(Response response) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException("응답 변환 실패", e);
        }
    }
}
