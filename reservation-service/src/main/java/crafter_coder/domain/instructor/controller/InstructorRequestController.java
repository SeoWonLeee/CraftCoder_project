package crafter_coder.domain.instructor.controller;

import crafter_coder.domain.course.dto.CourseDetailResponse;
import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.instructor.dto.InstructorRequestDto;
import crafter_coder.domain.instructor.service.InstructorRequestService;
import crafter_coder.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "강좌 수정 요청 (서버)", description = "관리자에게 강좌 수정을 요청했습니다.")
    public ResponseEntity<ResponseDto<CourseDetailResponse>> createUpdateRequest(
            @PathVariable Long instructorId,
            @PathVariable Long courseId,
            @RequestBody InstructorRequestDto requestDto) {
        Course updatedCourse = requestService.createUpdateRequest(
                instructorId, courseId, requestDto.getStatus(), requestDto.getDayOfWeek(),
                requestDto.getStartTime(), requestDto.getEndTime(), requestDto.getMaxCapacity(),
                requestDto.getStartDate(), requestDto.getEndDate());

        CourseDetailResponse response = CourseDetailResponse.fromEntity(updatedCourse);
        return ResponseEntity.ok(ResponseDto.of(response, "강좌 수정 요청 (서버)"));
    }

    @PostMapping("/courses/{courseId}/delete")
    @Operation(summary = "강좌 삭제 요청 (서버)", description = "관리자에게 강좌 삭제를 요청했습니다.")
    public ResponseEntity<ResponseDto<String>> createDeleteRequest(
            @PathVariable Long instructorId,
            @PathVariable Long courseId,
            @RequestBody InstructorRequestDto requestDto) {
        requestService.createDeleteRequest(instructorId, courseId, requestDto.getReason());
        return ResponseEntity.ok(ResponseDto.of(null, "강좌 삭제 요청 (서버)"));
    }

    @GetMapping
    @Operation(summary = "강좌 수정/삭제 요청 내역 조회", description = "강사가 요청한 강좌 수정/삭제 요청 내역을 조회합니다.")
    public ResponseEntity<ResponseDto<List<InstructorRequestDto>>> getRequestsByInstructor(
            @PathVariable Long instructorId) {
        List<InstructorRequestDto> requests = requestService.getRequestsByInstructor(instructorId).stream()
                .map(InstructorRequestDto::of)
                .toList();
        return ResponseEntity.ok(ResponseDto.of(requests, "강좌 수정/삭제 요청 내역 조회"));
    }
}
