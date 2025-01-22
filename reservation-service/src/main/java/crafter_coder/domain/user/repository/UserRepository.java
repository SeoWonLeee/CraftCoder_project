package crafter_coder.domain.user.repository;

import crafter_coder.domain.user.model.Role;
import crafter_coder.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByIdAndRole(Long id, Role role);
    Optional<User> findByUsername(String username);
}
