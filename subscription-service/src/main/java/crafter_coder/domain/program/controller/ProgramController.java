package crafter_coder.domain.program.controller;

import crafter_coder.domain.program.dto.ProgramReqDto;
import crafter_coder.domain.program.dto.ProgramResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions/programs")
public class ProgramController {

    @PostMapping("/register")
    public ResponseEntity<ProgramResDto> register(@RequestBody ProgramReqDto programReqDto) {
        return ResponseEntity.ok();
    }
}
