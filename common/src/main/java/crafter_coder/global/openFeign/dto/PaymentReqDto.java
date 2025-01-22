package crafter_coder.global.openFeign.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentReqDto(
        // 계좌 번호가 몇자리인 그런 규칙이 있으면 validation을 추가하면 좋을듯
        String withdrawAccountNumber,
        String depositAccountNumber,
        @Positive(message = "금액은 양수여야 합니다.")
        BigDecimal amount,
        String password
) {
    public static PaymentReqDto of(String withdrawAccountNumber, String depositAccountNumber, BigDecimal amount, String password) {
        return new PaymentReqDto(withdrawAccountNumber, depositAccountNumber, amount, password);
    }
}
