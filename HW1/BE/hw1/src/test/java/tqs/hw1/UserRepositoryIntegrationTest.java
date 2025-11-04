package tqs.hw1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import tqs.hw1.data.User;
import tqs.hw1.data.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource( locations = "application-integrationtest.properties")
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository repo;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        user1 = new User("john_doe", "password123", false);

        user2 = new User("jane_smith", "securePass456", false);

        user3 = new User("admin_user", "myPassword789", true);

        repo.save(user1);
        repo.save(user2);
        repo.save(user3);
    }

    @AfterEach
    void resetDb() {
        repo.deleteAll();
    }

    @Test
    void whenFindByUsernameAndPasswordWithCorrectCredentials_thenReturnUser() {
        Optional<User> found = repo.findByUsernameAndPassword("john_doe", "password123");

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("john_doe");
        assertThat(found.get().getPassword()).isEqualTo("password123");
    }

    @Test
    void whenFindByUsernameAndPasswordWithWrongPassword_thenReturnEmpty() {
        Optional<User> found = repo.findByUsernameAndPassword("john_doe", "wrongPassword");

        assertThat(found).isEmpty();
    }

    @Test
    void whenFindByUsernameAndPasswordWithWrongUsername_thenReturnEmpty() {
        Optional<User> found = repo.findByUsernameAndPassword("nonexistent_user", "password123");

        assertThat(found).isEmpty();
    }

    @Test
    void whenFindByUsernameAndPasswordWithBothWrong_thenReturnEmpty() {
        Optional<User> found = repo.findByUsernameAndPassword("wrong_user", "wrong_pass");

        assertThat(found).isEmpty();
    }

    @Test
    void whenExistsByUsernameWithExistingUser_thenReturnTrue() {
        boolean exists = repo.existsByUsername("john_doe");

        assertThat(exists).isTrue();
    }

    @Test
    void whenExistsByUsernameWithNonExistingUser_thenReturnFalse() {
        boolean exists = repo.existsByUsername("nonexistent_user");

        assertThat(exists).isFalse();
    }

    @Test
    void whenExistsByUsernameIsCaseSensitive_thenReturnCorrectResult() {
        boolean existsLowerCase = repo.existsByUsername("john_doe");
        boolean existsUpperCase = repo.existsByUsername("JOHN_DOE");

        assertThat(existsLowerCase).isTrue();
        assertThat(existsUpperCase).isFalse();
    }

    @Test
    void whenSaveUser_thenUserIsPersisted() {
        User newUser = new User("alice_brown", "alicePass123", false);

        User saved = repo.save(newUser);

        assertThat(repo.existsByUsername("alice_brown")).isTrue();

        Optional<User> found = repo.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("alice_brown");
    }

}