package crafter_coder.global.exception;

public record RestApiExceptionDto(
        String url,
        String message
) {
    public static RestApiExceptionDto of(String url, String message) {
        return new RestApiExceptionDto(url, message);
    }
}
