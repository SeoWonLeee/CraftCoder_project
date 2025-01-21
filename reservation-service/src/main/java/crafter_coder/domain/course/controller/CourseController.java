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

    @GetMapping
    @Operation(summary = "강좌 목록 조회", description = "전체 강좌 목록을 조회합니다.")
    public ResponseEntity<List<CourseListResponse>> getCourses() {
        List<CourseListResponse> response = courseService.getAllCourses();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}")
    @Operation(summary = "강좌 상세 조회", description = "강좌 ID로 강좌 상세 정보를 조회합니다.")
    public ResponseEntity<CourseDetailResponse> getCourseDetail(@PathVariable Long courseId) {
        return courseService.getCourseDetail(courseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "강좌 생성", description = "새로운 강좌를 생성합니다.")
    public ResponseEntity<Long> createCourse(@RequestBody CourseRequest courseRequest) {
        Long instructorId = 101L;
        validateInstructorRole(instructorId);
        Long courseId = courseService.createCourse(courseRequest, instructorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseId);
    }


    @PutMapping("/{courseId}")
    @Operation(summary = "강좌 수정", description = "강좌 ID로 기존 강좌를 수정합니다.")
    public ResponseEntity<Void> updateCourse(@PathVariable Long courseId, @RequestBody CourseRequest courseRequest) {
        Long instructorId = 101L;
        validateInstructorRole(instructorId);
        courseService.updateCourse(courseId, courseRequest, instructorId);
        return ResponseEntity.ok().build();
    }

    private void validateInstructorRole(Long instructorId) {
        if (instructorId == null || !isInstructor(instructorId)) {
            throw new AccessDeniedException("강사 권한이 필요합니다.");
        }
    }

    private boolean isInstructor(Long instructorId) {
        return true;
    }
}

