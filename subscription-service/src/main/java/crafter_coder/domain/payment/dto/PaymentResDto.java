package crafter_coder.domain.payment.dto;

public record PaymentResDto(
        Long paymentId
) {
    public static PaymentResDto of(Long paymentId) {
        return new PaymentResDto(paymentId);
    }
}
