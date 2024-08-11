package erykmarnik.hrm.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
  Optional<User> findByUserId(Long userId);
  Optional<User> findByUsername(String username);
  boolean existsByUserId(Long userId);
}
