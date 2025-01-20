package crafter_coder.domain.subscription.repository;

import crafter_coder.domain.subscription.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
