package crafter_coder.domain.course.controller;

import crafter_coder.domain.course.dto.CourseDetailResponse;
import crafter_coder.domain.course.dto.CourseListResponse;
import crafter_coder.domain.course.dto.CourseRequest;
import crafter_coder.domain.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // 강좌 목록 조회 API
    @GetMapping
    @Operation(summary = "강좌 목록 조회", description = "전체 강좌 목록을 조회합니다.")
    public ResponseEntity<List<CourseListResponse>> getCourses() {
        List<CourseListResponse> response = courseService.getAllCourses();
        return ResponseEntity.ok(response);
    }

    // 강좌 상세 조회 API
    @GetMapping("/{courseId}")
    @Operation(summary = "강좌 상세 조회", description = "강좌 ID로 강좌 상세 정보를 조회합니다.")
    public ResponseEntity<CourseDetailResponse> getCourseDetail(@PathVariable Long courseId) {
        return courseService.getCourseDetail(courseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 강좌 생성 API
    @PostMapping
    @Operation(summary = "강좌 생성", description = "새로운 강좌를 생성합니다.")
    public ResponseEntity<Long> createCourse(@RequestBody CourseRequest courseRequest) {
        // 강사 권한 확인 필요 (임시 ID 사용)
        Long instructorId = 101L; // 실제 환경에서는 인증된 사용자 ID를 사용
        validateInstructorRole(instructorId); // 권한 확인
        Long courseId = courseService.createCourse(courseRequest, instructorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseId);
    }


    // 강좌 수정 API
    @PutMapping("/{courseId}")
    @Operation(summary = "강좌 수정", description = "강좌 ID로 기존 강좌를 수정합니다.")
    public ResponseEntity<Void> updateCourse(@PathVariable Long courseId, @RequestBody CourseRequest courseRequest) {
        // 강사 권한 확인 필요 (임시 ID 사용)
        Long instructorId = 101L; // 실제 환경에서는 인증된 사용자 ID를 사용
        validateInstructorRole(instructorId); // 권한 확인
        courseService.updateCourse(courseId, courseRequest, instructorId);
        return ResponseEntity.ok().build();
    }

    // 강사 권한 확인 (임시 구현)
    private void validateInstructorRole(Long instructorId) {
        // 강사 권한이 없는 경우 예외 발생
        if (instructorId == null || !isInstructor(instructorId)) {
            throw new AccessDeniedException("강사 권한이 필요합니다.");
        }
    }

    private boolean isInstructor(Long instructorId) {
        // 임시로 true 반환. 실제 환경에서는 사용자 정보를 조회하여 강사 여부 확인
        return true;
    }
}

