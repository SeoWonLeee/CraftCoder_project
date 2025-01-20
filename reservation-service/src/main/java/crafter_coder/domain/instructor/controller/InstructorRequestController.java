package crafter_coder.domain.instructor.controller;

import crafter_coder.domain.instructor.dto.InstructorRequestDto;
import crafter_coder.domain.instructor.service.InstructorRequestService;
import crafter_coder.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instructors/{instructorId}/requests")
@RequiredArgsConstructor
public class InstructorRequestController {

    private final InstructorRequestService requestService;

    @PostMapping("/courses/{courseId}/update")
    public ResponseEntity<ResponseDto<String>> createUpdateRequest(
            @PathVariable Long instructorId,
            @PathVariable Long courseId,
            @RequestBody InstructorRequestDto requestDto) {
        requestService.createUpdateRequest(
                instructorId, courseId, requestDto.getStatus(), requestDto.getDayOfWeek(),
                requestDto.getStartTime(), requestDto.getEndTime(), requestDto.getMaxCapacity(),
                requestDto.getStartDate(), requestDto.getEndDate());
        return ResponseEntity.ok(ResponseDto.of(null, "강좌 수정 요청"));
    }

    @PostMapping("/courses/{courseId}/delete")
    public ResponseEntity<ResponseDto<String>> createDeleteRequest(
            @PathVariable Long instructorId,
            @PathVariable Long courseId,
            @RequestBody InstructorRequestDto requestDto) {
        requestService.createDeleteRequest(instructorId, courseId, requestDto.getReason());
        return ResponseEntity.ok(ResponseDto.of(null, "강좌 삭제 요청"));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<InstructorRequestDto>>> getRequestsByInstructor(
            @PathVariable Long instructorId) {
        List<InstructorRequestDto> requests = requestService.getRequestsByInstructor(instructorId).stream()
                .map(InstructorRequestDto::of)
                .toList();
        return ResponseEntity.ok(ResponseDto.of(requests, "요청 내역 조회"));
    }
}
