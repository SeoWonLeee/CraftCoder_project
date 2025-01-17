package crafter_coder.domain.user.repository;

import crafter_coder.domain.user.model.Role;
import crafter_coder.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByIdAndRole(Long id, Role role);
}
