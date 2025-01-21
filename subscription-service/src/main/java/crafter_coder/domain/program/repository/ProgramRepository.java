package crafter_coder.domain.program.repository;

import crafter_coder.domain.program.model.Program;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    @Query("SELECT DISTINCT p FROM Program p JOIN FETCH p.subscriptions WHERE p.billingDate = :billingDate")
    List<Program> findByBillingDateWithSubscriptions(@Param("billingDate") int billingDate);

    boolean existsByName(String name);
}
