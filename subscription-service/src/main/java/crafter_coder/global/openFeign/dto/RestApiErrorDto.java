package crafter_coder.global.openFeign.dto;

public record RestApiErrorDto(
        String url,
        String message
) {
    public static RestApiErrorDto of(String url, String message) {
        return new RestApiErrorDto(url, message);
    }
}
