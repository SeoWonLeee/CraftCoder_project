package crafter_coder.domain.program.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import crafter_coder.domain.program.model.Program;
import crafter_coder.domain.program.model.ProgramStatus;
import lombok.AccessLevel;
import lombok.Builder;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(access = AccessLevel.PRIVATE)
public record ProgramResDto(
        Long programId,
        String name,
        BigDecimal price,
        Integer billingDate,
        String accountNumber,
        ProgramStatus status
) {
    public static ProgramResDto of(Long programId) {
        return ProgramResDto.builder()
                .programId(programId)
                .build();
    }

    public static ProgramResDto fromEntity(Program program) {
        return ProgramResDto.builder()
                .programId(program.getId())
                .name(program.getName())
                .price(program.getPrice())
                .billingDate(program.getBillingDate())
                .accountNumber(program.getAccountNumber())
                .status(program.getStatus())
                .build();
    }
}
