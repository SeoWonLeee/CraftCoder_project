package crafter_coder.domain.course.service;

import crafter_coder.domain.course.dto.CourseDetailResponse;
import crafter_coder.domain.course.dto.CourseListResponse;
import crafter_coder.domain.course.dto.CourseRequest;
import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.course.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    //전체 강좌 목록 가져오기
    public List<CourseListResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(CourseListResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //강좌 상세 가져오기
    public Optional<CourseDetailResponse> getCourseDetail(Long courseId) {
        return courseRepository.findById(courseId)
                .map(CourseDetailResponse::fromEntity);
    }

    // 강좌 생성
    public Long createCourse(CourseRequest request, Long instructorId) {
        Course course = request.toEntity(instructorId);
        courseRepository.save(course);
        return course.getId();
    }

    // 강좌 수정
    public void updateCourse(Long courseId, CourseRequest request, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("해당 강좌를 찾을 수 없습니다."));
        if (!course.getInstructorId().equals(instructorId)) {
         //   throw new AccessDeniedException("해당 강좌를 수정할 권한이 없습니다.");
        }
        course.update(request);
    }
}




