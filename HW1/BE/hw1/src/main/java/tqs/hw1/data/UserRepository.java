package tqs.hw1.data;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
    public Optional<User> findByUsernameAndPassword(String username, String password);
    public boolean existsByUsername(String username);
}
