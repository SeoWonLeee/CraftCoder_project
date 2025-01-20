package crafter_coder.domain.program.service;

import crafter_coder.domain.program.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramService {
    private final ProgramRepository programRepository;

}
