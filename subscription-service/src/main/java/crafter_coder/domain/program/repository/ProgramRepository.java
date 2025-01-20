package crafter_coder.domain.program.repository;

import crafter_coder.domain.program.model.Program;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    List<Program> findByBillingDate(int billingDate);

    boolean existsByName(String name);
}
