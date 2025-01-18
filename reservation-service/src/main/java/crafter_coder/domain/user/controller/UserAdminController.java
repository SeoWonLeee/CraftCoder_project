package crafter_coder.domain.user.controller;

import crafter_coder.domain.user.dto.UserRecordResponseDto;
import crafter_coder.domain.user.dto.UserResponseDto;
import crafter_coder.domain.user.service.UserAdminService;
import crafter_coder.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    @GetMapping
    public ResponseEntity<ResponseDto<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userAdminService.getAllUsers();
        return ResponseEntity.ok(ResponseDto.of(users, "회원 목록 조회 성공"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<UserResponseDto>> getUserById(@PathVariable Long id) {
        UserResponseDto user = userAdminService.getUserById(id);
        return ResponseEntity.ok(ResponseDto.of(user, "회원 정보 조회 성공"));
    }

    @GetMapping("/{id}/records")
    public ResponseEntity<ResponseDto<UserRecordResponseDto>> getUserRecords(@PathVariable Long id) {
        UserRecordResponseDto records = userAdminService.getUserRecords(id);
        return ResponseEntity.ok(ResponseDto.of(records, "회원 수강 및 결제 기록 조회 성공"));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseDto<String>> updateUserStatus(@PathVariable Long id, @RequestBody String status) {
        userAdminService.updateUserStatus(id, status);
        return ResponseEntity.ok(ResponseDto.of(null, "회원 상태 변경 성공"));
    }
}
