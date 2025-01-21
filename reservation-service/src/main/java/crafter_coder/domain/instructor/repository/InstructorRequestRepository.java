package crafter_coder.domain.instructor.repository;

import crafter_coder.domain.instructor.model.InstructorRequest;
import crafter_coder.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstructorRequestRepository extends JpaRepository<InstructorRequest, Long> {
    List<InstructorRequest> findByInstructor(User instructor);
    @Query("SELECT r FROM InstructorRequest r WHERE (:status IS NULL OR r.status = :status)")
    List<InstructorRequest> findByStatus(@Param("status") String status);
}
