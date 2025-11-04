package tqs.hw1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import tqs.hw1.data.User;
import tqs.hw1.data.UserRepository;
import tqs.hw1.services.UserServiceImp;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImp userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("john_doe", "securePass123", false);
    }

    @Test
    void whenCreateUser_thenUserIsSaved() {
        String name = "john_doe";
        String password = "securePass123";
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User created = userService.createUser(name, password, false);

        assertThat(created).isNotNull();
        assertThat(created.getUsername()).isEqualTo(name);
        assertThat(created.getPassword()).isEqualTo(password);
        assertThat(created.getIsStaff()).isFalse();
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void whenCreateStaffUser_thenStaffFlagIsSet() {
        User staffUser = new User("staff_member", "staffPass", true);
        when(userRepository.save(any(User.class))).thenReturn(staffUser);

        User created = userService.createUser("staff_member", "staffPass", true);

        assertThat(created.getIsStaff()).isTrue();
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void whenGetUserWithValidCredentials_thenUserIsReturned() {
        String name = "john_doe";
        String password = "securePass123";
        when(userRepository.findByUsernameAndPassword(name, password))
            .thenReturn(Optional.of(testUser));

        User found = userService.getUser(name, password);

        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo(name);
        verify(userRepository, times(1)).findByUsernameAndPassword(name, password);
    }

    @Test
    void whenGetUserWithInvalidCredentials_thenThrowException() {
        String name = "john_doe";
        String wrongPassword = "wrongPass";
        when(userRepository.findByUsernameAndPassword(name, wrongPassword))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(name, wrongPassword))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("User not found");
        
        verify(userRepository, times(1)).findByUsernameAndPassword(name, wrongPassword);
    }

    @Test
    void whenGetUserWithNonExistentUsername_thenThrowException() {
        String invalidName = "nonexistent";
        String password = "anyPass";
        when(userRepository.findByUsernameAndPassword(invalidName, password))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(invalidName, password))
            .isInstanceOf(EntityNotFoundException.class);

        verify(userRepository, times(1)).findByUsernameAndPassword(invalidName, password);
    }

    @Test
    void whenCreateUserWithExistingUsername_thenThrowException() {
        String name = "john_doe";
        String password = "newPass";
        when(userRepository.existsByUsername(name)).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(name, password, false))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Username already in use");

        verify(userRepository, times(1)).existsByUsername(name);
        verify(userRepository, never()).save(any(User.class));
    }
}