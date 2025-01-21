package crafter_coder.domain.instructor.controller;

import crafter_coder.domain.instructor.dto.InstructorRequestDto;
import crafter_coder.domain.instructor.service.InstructorRequestService;
import crafter_coder.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/instructors/requests")
@RequiredArgsConstructor
public class AdminInstructorRequestController {

    private final InstructorRequestService requestService;

    @GetMapping
    public ResponseEntity<ResponseDto<List<InstructorRequestDto>>> getAllRequests(
            @RequestParam(required = false) String status) {
        List<InstructorRequestDto> requests = requestService.getAllRequests(status);
        return ResponseEntity.ok(ResponseDto.of(requests, "관리자 권한으로 모든 강사 요청 목록 조회"));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ResponseDto<InstructorRequestDto>> getRequestDetail(@PathVariable Long requestId) {
        InstructorRequestDto requestDetail = requestService.getRequestDetail(requestId);
        return ResponseEntity.ok(ResponseDto.of(requestDetail, "관리자 권한으로 요청 상세 정보 조회"));
    }

    @PutMapping("/{requestId}/approve")
    public ResponseEntity<ResponseDto<Void>> approveRequest(@PathVariable Long requestId) {
        requestService.approveRequest(requestId);
        return ResponseEntity.ok(ResponseDto.of(null, "요청 승인 완료"));
    }

    @PutMapping("/{requestId}/reject")
    public ResponseEntity<ResponseDto<Void>> rejectRequest(
            @PathVariable Long requestId,
            @RequestBody String reason) {
        requestService.rejectRequest(requestId, reason);
        return ResponseEntity.ok(ResponseDto.of(null, "요청 거부 완료"));
    }
}
