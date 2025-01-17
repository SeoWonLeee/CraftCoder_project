package crafter_coder.domain.user.repository;

import crafter_coder.domain.user.model.Role;
import crafter_coder.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByIdAndRole(Long id, Role role);
    Optional<User> findById(Integer id);
    Optional<User> findByUsername(String username);


}
