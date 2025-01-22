package crafter_coder.domain.user.controller;

import crafter_coder.domain.user.dto.UserCourseReservationDto;
import crafter_coder.domain.user.dto.UserRecordResponseDto;
import crafter_coder.domain.user.dto.UserResponseDto;
import crafter_coder.domain.user.service.UserAdminService;
import crafter_coder.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    @GetMapping
    @Operation(summary = "회원 목록 조회", description = "회원 목록을 조회합니다.")
    public ResponseEntity<ResponseDto<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userAdminService.getAllUsers();
        return ResponseEntity.ok(ResponseDto.of(users, "회원 목록 조회 성공"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회합니다.")
    public ResponseEntity<ResponseDto<UserResponseDto>> getUserById(@PathVariable Long id) {
        UserResponseDto user = userAdminService.getUserById(id);
        return ResponseEntity.ok(ResponseDto.of(user, "회원 정보 조회 성공"));
    }

    @GetMapping("/{id}/records")
    @Operation(summary = "회원 수강 및 결제 기록 조회", description = "회원 수강 및 결제 기록을 조회합니다.")
    public ResponseEntity<ResponseDto<UserRecordResponseDto>> getUserRecords(@PathVariable Long id) {
        UserRecordResponseDto records = userAdminService.getUserRecords(id);
        return ResponseEntity.ok(ResponseDto.of(records, "회원 수강 및 결제 기록 조회 성공"));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "회원 상태 변경", description = "회원 상태를 변경합니다.")
    public ResponseEntity<ResponseDto<Map<String, String>>> updateUserStatus(@PathVariable Long id, @RequestBody String status) {
        String oldStatus = userAdminService.updateUserStatus(id, status);
        Map<String, String> result = new HashMap<>();
        result.put("oldStatus", oldStatus);
        result.put("newStatus", status.toUpperCase());
        return ResponseEntity.ok(ResponseDto.of(result, "회원 상태 변경 성공"));
    }

    @GetMapping("/{id}/courses")
    @Operation(summary = "회원 강좌 예약 현황 조회", description = "회원 강좌 예약 현황을 조회합니다.")
    public ResponseEntity<ResponseDto<List<UserCourseReservationDto>>> getUserCourseReservations(@PathVariable Long id) {
        List<UserCourseReservationDto> reservations = userAdminService.getUserCourseReservations(id);
        return ResponseEntity.ok(ResponseDto.of(reservations, "회원 강좌 예약 현황 조회 성공"));
    }

}
