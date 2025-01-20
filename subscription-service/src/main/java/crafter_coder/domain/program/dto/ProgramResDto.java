package crafter_coder.domain.program.dto;

public record ProgramResDto(
        Long programId
) {
    public static ProgramResDto of(Long programId) {
        return new ProgramResDto(programId);
    }
}
