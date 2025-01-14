package crafter_coder.domain.course.controller;

import crafter_coder.domain.course.dto.CourseDetailResponse;
import crafter_coder.domain.course.dto.CourseListResponse;
import crafter_coder.domain.course.dto.UpdateCourseRequest;
import crafter_coder.domain.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // 강좌 목록 조회 API
    @GetMapping
    public ResponseEntity<List<CourseListResponse>> getCourses() {
        List<CourseListResponse> response = courseService.getAllCourses();
        return ResponseEntity.ok(response);
    }

    // 강좌 상세 조회 API
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDetailResponse> getCourseDetail(@PathVariable Long courseId) {
        return courseService.getCourseDetail(courseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    // 강좌 생성 API
//    @PostMapping
//    public ResponseEntity<CourseDetailResponse> createCourse(@Valid @RequestBody CreateCourseRequest request) {
//        CourseDetailResponse response = courseService.createCourse(request);
//        return ResponseEntity.ok(response);
//    }
//
//    // 강좌 수정 API
//    @PutMapping("/{courseId}")
//    public ResponseEntity<CourseDetailResponse> updateCourse(
//            @PathVariable Long courseId,
//            @Valid @RequestBody UpdateCourseRequest request) {
//        CourseDetailResponse response = courseService.updateCourse(courseId, request);
//        return ResponseEntity.ok(response);
//    }
}
