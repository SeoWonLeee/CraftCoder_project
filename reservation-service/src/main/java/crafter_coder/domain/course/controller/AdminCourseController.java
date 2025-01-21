package crafter_coder.domain.course.controller;

import crafter_coder.domain.course.dto.CourseDetailResponse;
import crafter_coder.domain.course.dto.CourseListResponse;
import crafter_coder.domain.course.dto.CourseRequest;
import crafter_coder.domain.course.service.CourseService;
import crafter_coder.domain.user.dto.UserResponseDto;
import crafter_coder.domain.user.service.InstructorService;
import crafter_coder.global.dto.ResponseDto;
import crafter_coder.global.exception.MyErrorCode;
import crafter_coder.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {
// public class CourseController {

    private final CourseService courseService;
    private final InstructorService instructorService;

    @GetMapping
    public ResponseEntity<ResponseDto<List<CourseListResponse>>> getCourses() {
        List<CourseListResponse> response = courseService.getAllCourses();
        return ResponseEntity.ok(ResponseDto.of(response, "전체 강좌 목록 조회"));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<ResponseDto<CourseDetailResponse>> getCourseDetail(@PathVariable Long courseId) {
        return courseService.getCourseDetail(courseId)
                .map(detail -> ResponseEntity.ok(ResponseDto.of(detail, "강좌 상세 정보 조회")))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Long>> createCourse(
            @RequestBody CourseRequest courseRequest,
            @RequestParam Long instructorId
    ) {
        validateInstructorRole(instructorId);
        Long courseId = courseService.createCourse(courseRequest, instructorId);
        return ResponseEntity.status(201).body(ResponseDto.of(courseId, "강좌 생성"));
    }

    @PutMapping("/{courseId}")
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
    public ResponseEntity<ResponseDto<List<CourseListResponse>>> getCoursesByCategory(
            @PathVariable String category) {
        List<CourseListResponse> courses = courseService.getCoursesByCategory(category);
        return ResponseEntity.ok(ResponseDto.of(courses, "카테고리별 강좌 목록 조회"));
    }

    @GetMapping("/{courseId}/users")
    public ResponseEntity<ResponseDto<List<UserResponseDto>>> getCourseUsers(@PathVariable Long courseId) {
        List<UserResponseDto> users = courseService.getCourseUsers(courseId);
        return ResponseEntity.ok(ResponseDto.of(users, "강좌 수강 회원 목록 조회"));
    }

    private void validateInstructorRole(Long instructorId) {
        if (instructorId == null || !instructorService.isValidInstructor(instructorId)) {
            throw new MyException(MyErrorCode.INSTRUCTOR_ONLY);
        }
    }
}
