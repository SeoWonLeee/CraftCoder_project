package crafter_coder.domain.course.controller;

import crafter_coder.domain.course.dto.CourseDetailResponse;
import crafter_coder.domain.course.dto.CourseListResponse;
import crafter_coder.domain.course.dto.CourseRequest;
import crafter_coder.domain.course.service.CourseService;
import crafter_coder.domain.user.dto.UserResponseDto;
import crafter_coder.domain.user.service.UserService;
import crafter_coder.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseService courseService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "전체 강좌 목록 조회", description = "모든 강좌를 조회합니다.")
    public ResponseEntity<ResponseDto<List<CourseListResponse>>> getCourses() {
        List<CourseListResponse> response = courseService.getAllCourses();
        return ResponseEntity.ok(ResponseDto.of(response, "전체 강좌 목록 조회"));
    }

    @GetMapping("/{courseId}")
    @Operation(summary = "강좌 상세 조회", description = "강좌 ID로 강좌 상세 정보를 조회합니다.")
    public ResponseEntity<ResponseDto<CourseDetailResponse>> getCourseDetail(@PathVariable Long courseId) {
        return courseService.getCourseDetail(courseId)
                .map(detail -> ResponseEntity.ok(ResponseDto.of(detail, "강좌 상세 정보 조회")))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "강좌 생성", description = "새로운 강좌를 생성합니다.")
    public ResponseEntity<ResponseDto<Long>> createCourse(
            @RequestBody CourseRequest courseRequest,
            @RequestParam Long instructorId
    ) {
        validateInstructorRole(instructorId);
        Long courseId = courseService.createCourse(courseRequest, instructorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.of(courseId, "강좌 생성"));
    }

    @PutMapping("/{courseId}")
    @Operation(summary = "강좌 수정", description = "강좌 ID로 기존 강좌를 수정합니다.")
    public ResponseEntity<ResponseDto<Void>> updateCourse(
            @PathVariable Long courseId,
            @RequestBody CourseRequest courseRequest,
            @RequestParam Long instructorId
    ) {
        validateInstructorRole(instructorId);
        courseService.updateCourse(courseId, courseRequest, instructorId);
        return ResponseEntity.ok(ResponseDto.of(null, "강좌 수정"));
    }

    @GetMapping("/categories/{category}")
    @Operation(summary = "카테고리별 강좌 목록 조회", description = "카테고리별 강좌 목록을 조회합니다.")
    public ResponseEntity<ResponseDto<List<CourseListResponse>>> getCoursesByCategory(
            @PathVariable String category) {
        List<CourseListResponse> courses = courseService.getCoursesByCategory(category);
        return ResponseEntity.ok(ResponseDto.of(courses, "카테고리별 강좌 목록 조회"));
    }

    @GetMapping("/{courseId}/users")
    @Operation(summary = "강좌 수강 회원 목록 조회", description = "강좌에 수강한 회원 목록을 조회합니다.")
    public ResponseEntity<ResponseDto<List<UserResponseDto>>> getCourseUsers(@PathVariable Long courseId) {
        List<UserResponseDto> users = courseService.getCourseUsers(courseId);
        return ResponseEntity.ok(ResponseDto.of(users, "강좌 수강 회원 목록 조회"));
    }

    @PutMapping("/{courseId}/status")
    @Operation(summary = "강좌 상태 변경", description = "강좌의 상태를 변경합니다.")
    public ResponseEntity<ResponseDto<String>> updateCourseStatus(
            @PathVariable Long courseId,
            @RequestBody String status) {
        courseService.updateCourseStatus(courseId, status);
        return ResponseEntity.ok(ResponseDto.of(null, "강좌 상태 변경 성공"));
    }

    // 강사 권한 확인
    private void validateInstructorRole(Long instructorId) {
        if (instructorId == null || !userService.isInstructor(instructorId)) {
            throw new AccessDeniedException("강사 권한이 필요합니다.");
        }
    }
}
