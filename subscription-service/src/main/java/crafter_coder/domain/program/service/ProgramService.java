package crafter_coder.domain.program.service;

import crafter_coder.domain.program.dto.ProgramReqDto;
import crafter_coder.domain.program.dto.ProgramResDto;
import crafter_coder.domain.program.exception.ProgramException;
import crafter_coder.domain.program.exception.ProgramException.ProgramAlreadyExistsException;
import crafter_coder.domain.program.model.Program;
import crafter_coder.domain.program.model.ProgramStatus;
import crafter_coder.domain.program.repository.ProgramRepository;
import crafter_coder.openFeign.client.AccountApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgramService {
    private final ProgramRepository programRepository;
    private final AccountApiClient accountApiClient;

    @Transactional
    public ProgramResDto register(ProgramReqDto programReqDto) {

        checkIfProgramNameAlreadyExists(programReqDto);
        // 전달받은 계좌 번호가 실제 존재하는지 확인을 해야할거같은데
//        accountApiClient.checkAccountNumber(programReqDto.accountNumber());

        Program program = ProgramReqDto.toEntity(programReqDto);
        programRepository.save(program);
        return ProgramResDto.of(program.getId());
    }

    private void checkIfProgramNameAlreadyExists(ProgramReqDto programReqDto) {
        if(programRepository.existsByName(programReqDto.name())) {
            throw new ProgramAlreadyExistsException();
        }
    }

    @Transactional
    public ProgramResDto delete(Long id) {
        Program program = findProgramById(id);
        program.softDelete();
        return ProgramResDto.of(program.getId());
    }

    private Program findProgramById(Long id) {
        return programRepository.findById(id)
                .orElseThrow(ProgramException.ProgramNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public ProgramResDto getProgram(Long id) {
        Program program = findProgramById(id);
        return ProgramResDto.fromEntity(program);
    }
}
