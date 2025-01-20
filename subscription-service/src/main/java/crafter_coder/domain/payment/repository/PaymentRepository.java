package crafter_coder.domain.payment.repository;

import crafter_coder.domain.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
