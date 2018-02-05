package th.co.maximus.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import th.co.maximus.auth.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
	
    User findByUsername(String username);
}
