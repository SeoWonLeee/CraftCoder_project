package crafter_coder.domain.program.repository;

import crafter_coder.domain.program.model.Program;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    List<Program> findByBillingDate(@NotNull int billingDate);
}
