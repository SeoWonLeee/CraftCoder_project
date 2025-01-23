//package crafter_coder.domain.program.service;
//
//import crafter_coder.domain.program.dto.ProgramReqDto;
//import crafter_coder.domain.program.dto.ProgramResDto;
//import crafter_coder.domain.program.exception.ProgramException;
//import crafter_coder.domain.program.exception.ProgramException.ProgramAlreadyExistsException;
//import crafter_coder.domain.program.model.Program;
//import crafter_coder.domain.program.repository.ProgramRepository;
//import org.aspectj.lang.annotation.After;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//
//import java.math.BigDecimal;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//@SpringBootTest
//@ActiveProfiles("test")
//class ProgramServiceTest {
//
//    @Autowired
//    private ProgramService programService;
//
//    @Autowired
//    private ProgramRepository programRepository;
//
//    @AfterEach
//    public void after() {
//        programRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("정기 결제 프로그램을 등록한다.")
//    void registerProgram() {
//        // given
//        ProgramReqDto programReqDto = ProgramReqDto.of("프로그램", BigDecimal.valueOf(10000), 1, "123412341234");
//
//        // when
//        ProgramResDto response = programService.register(programReqDto);
//
//        // then
//        assertThat(response.programId()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("등록하려는 정기 결제 프로그램의 이름이 중복되면 예외가 발생한다.")
//    void registerDuplicatedProgram() {
//        // given
//        ProgramReqDto programReqDto = ProgramReqDto.of("프로그램", BigDecimal.valueOf(10000), 1, "123412341234");
//        programService.register(programReqDto);
//
//        // when, then
//        assertThatThrownBy(() -> programService.register(programReqDto))
//                .isInstanceOf(ProgramAlreadyExistsException.class)
//                .hasMessageContaining("프로그램 이름이 이미 존재합니다.");
//    }
//}