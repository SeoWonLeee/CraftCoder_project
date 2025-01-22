package crafter_coder.domain.program.repository;

import crafter_coder.domain.program.model.Program;
import crafter_coder.domain.program.model.ProgramStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    // 스케줄러에서 매일 자정에 한번씩 쿼리하는거기 때문에 굳이 조회 성능을 위해 인덱스를 만들 필요는 없을듯..?
    @Query("SELECT DISTINCT p FROM Program p JOIN FETCH p.subscriptions WHERE p.billingDate = :billingDate AND p.status = :status")
    List<Program> findByBillingDateAndStatusWithSubscriptions(@Param("billingDate") int billingDate, @Param("status") ProgramStatus status);

    boolean existsByName(String name);
}
