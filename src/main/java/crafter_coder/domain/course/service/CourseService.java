package crafter_coder.domain.course.service;

import crafter_coder.domain.course.dto.CourseDetailResponse;
import crafter_coder.domain.course.dto.CourseListResponse;
import crafter_coder.domain.course.dto.CreateCourseRequest;
import crafter_coder.domain.course.dto.UpdateCourseRequest;
import crafter_coder.domain.course.model.Course;
import crafter_coder.domain.course.repository.CourseRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
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

//    // 강좌 개설
//    public CourseDetailResponse createCourse(CreateCourseRequest request) {
//        Course newCourse = request.toEntity();
//        Course savedCourse = courseRepository.save(newCourse);
//        return CourseDetailResponse.fromEntity(savedCourse);
//    }
//
//    // 강좌 수정
//    public CourseDetailResponse updateCourse(Long courseId, UpdateCourseRequest request) {
//        Course existingCourse = courseRepository.findById(courseId)
//                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
//
//        request.updateEntity(existingCourse);
//
//        Course updatedCourse = courseRepository.save(existingCourse);
//        return CourseDetailResponse.fromEntity(updatedCourse);
//    }
}

