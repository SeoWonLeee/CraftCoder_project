package crafter_coder.domain.instructor.controller;

import crafter_coder.domain.instructor.dto.CourseUpdateRequestDto;
import crafter_coder.domain.instructor.dto.InstructorResponseDto;
import crafter_coder.domain.instructor.service.AdminInstructorService;
import crafter_coder.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/instructors")
@RequiredArgsConstructor
public class AdminInstructorController {

    private final AdminInstructorService instructorService;

    @GetMapping
    @Operation(summary = "강사 목록 조회", description = "강사 목록을 조회합니다.")
    public ResponseEntity<ResponseDto<List<InstructorResponseDto>>> getAllInstructors() {
        List<InstructorResponseDto> instructors = instructorService.getAllInstructors();
        return ResponseEntity.ok(ResponseDto.of(instructors, "강사 목록 조회"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "강사 정보 조회", description = "강사 정보를 조회합니다.")
    public ResponseEntity<ResponseDto<InstructorResponseDto>> getInstructorById(@PathVariable Long id) {
        InstructorResponseDto instructor = instructorService.getInstructorById(id);
        return ResponseEntity.ok(ResponseDto.of(instructor, "강사 정보 조회"));
    }

    @PutMapping("/{instructorId}/courses/{courseId}")
    @Operation(summary = "강좌 수정 (강사 요청)", description = "강사 요청에 따라 강좌를 수정합니다.")
    public ResponseEntity<ResponseDto<String>> updateCourse(
            @PathVariable Long instructorId,
            @PathVariable Long courseId,
            @RequestBody CourseUpdateRequestDto request) {
        instructorService.updateCourse(instructorId, courseId, request);
        return ResponseEntity.ok(ResponseDto.of(null, "강좌 수정"));
    }

    @DeleteMapping("/{instructorId}/courses/{courseId}")
    @Operation(summary = "강좌 삭제 (강사 요청)", description = "강사 요청에 따라 강좌를 삭제합니다.")
    public ResponseEntity<ResponseDto<String>> deleteCourse(
            @PathVariable Long instructorId,
            @PathVariable Long courseId) {
        instructorService.deleteCourse(instructorId, courseId);
        return ResponseEntity.ok(ResponseDto.of(null, "강좌 삭제"));
    }

    @PutMapping("/{instructorId}/courses/{courseId}/status")
    @Operation(summary = "강좌 상태 변경 (강사 요청)", description = "강사 요청에 따라 강좌 상태를 변경합니다.")
    public ResponseEntity<ResponseDto<String>> updateCourseStatus(
            @PathVariable Long instructorId,
            @PathVariable Long courseId,
            @RequestBody String status) {
        instructorService.updateCourseStatus(instructorId, courseId, status);
        return ResponseEntity.ok(ResponseDto.of(null, "강좌 상태 변경"));
    }
}
