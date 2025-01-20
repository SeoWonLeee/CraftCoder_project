package crafter_coder.domain.program.dto;

import crafter_coder.domain.program.model.Program;
import crafter_coder.domain.program.model.ProgramStatus;
import jakarta.validation.constraints.*;

public record ProgramReqDto(
        @NotBlank(message = "프로그램 이름은 필수입니다.")
        @Size(max = 255, message = "프로그램 이름은 255자를 초과할 수 없습니다.")
        String name,

        @Positive(message = "가격은 양수여야 합니다.")
        int price,

        @Min(value = 1, message = "결제일은 1일부터 가능합니다.")
        @Max(value = 28, message = "결제일은 28일까지 가능합니다.")
        int billingDate,

        String accountNumber
) {
    public static ProgramReqDto of(String name, int price, int billingDate, String accountNumber) {
        return new ProgramReqDto(name, price, billingDate, accountNumber);
    }

    public static Program toEntity(ProgramReqDto programReqDto) {
        return Program.of(programReqDto.name(), programReqDto.price(), programReqDto.billingDate(), programReqDto.accountNumber(), ProgramStatus.ACTIVE);
    }
}
