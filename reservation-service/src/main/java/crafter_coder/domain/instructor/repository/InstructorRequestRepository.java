package crafter_coder.domain.instructor.repository;

import crafter_coder.domain.instructor.model.InstructorRequest;
import crafter_coder.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstructorRequestRepository extends JpaRepository<InstructorRequest, Long> {
    List<InstructorRequest> findByInstructor(User instructor);
}
