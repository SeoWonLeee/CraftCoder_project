package crafter_coder.domain.program.controller;

import crafter_coder.domain.program.dto.ProgramReqDto;
import crafter_coder.domain.program.dto.ProgramResDto;
import crafter_coder.domain.program.service.ProgramService;
import crafter_coder.global.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/programs")
public class ProgramController {

    private final ProgramService programService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<ProgramResDto>> register(@Valid @RequestBody ProgramReqDto programReqDto) {
        ProgramResDto programResDto = programService.register(programReqDto);
        return ResponseEntity.ok(ResponseDto.success(programResDto));
    }

    // 원래는 헤더로 액세스 토큰을 받아서 권한이 있는 경우에만 삭제할 수 있도록 해야함
    // TODO: 이 서비스에서 프로그램 비밀번호를 엔티티에 저장하고 인증 시 액세스 토큰을 발급해주도록
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDto<ProgramResDto>> delete(@PathVariable Long id) {
        ProgramResDto response = programService.delete(id);
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<ProgramResDto>> getProgram(@PathVariable Long id) {
        ProgramResDto response = programService.getProgram(id);
        return ResponseEntity.ok(ResponseDto.success(response));
    }
}
