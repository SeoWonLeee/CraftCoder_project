package crafter_coder.global.openFeign.dto;

public record PaymentResDto(
        Long paymentId
) {
    public static PaymentResDto of(Long paymentId) {
        return new PaymentResDto(paymentId);
    }
}
