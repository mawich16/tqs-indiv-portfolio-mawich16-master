package tqs.hw1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.hw1.boundary.UserController;
import tqs.hw1.data.User;
import tqs.hw1.dto.LoginRequest;
import tqs.hw1.dto.UserRequest;
import tqs.hw1.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User testUser;
    private UserRequest userRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new User("john_doe", "password123", false);

        userRequest = new UserRequest();
        userRequest.setUsername("john_doe");
        userRequest.setPassword("password123");
        userRequest.setStaff(false);

        loginRequest = new LoginRequest();
        loginRequest.setUsername("john_doe");
        loginRequest.setPassword("password123");
    }

    @Test
    void whenPostUser_thenCreateUser() throws Exception {
        when(userService.createUser(any(), any(), anyBoolean())).thenReturn(testUser);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.password").value("password123"))
                .andExpect(jsonPath("$.isStaff").value(false));

        verify(userService, times(1)).createUser(any(), any(), anyBoolean());
    }

    @Test
    void whenPostUserWithExistingUsername_thenBadRequest() throws Exception {
        when(userService.createUser(any(), any(), anyBoolean()))
                .thenThrow(new IllegalArgumentException("Username already in use"));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).createUser(any(), any(), anyBoolean());
    }

    @Test
    void whenLoginWithValidCredentials_thenReturnUser() throws Exception {
        when(userService.getUser(any(), any())).thenReturn(testUser);

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.password").value("password123"));

        verify(userService, times(1)).getUser(any(), any());
    }

    @Test
    void whenLoginWithInvalidCredentials_thenUnauthorized() throws Exception {
        when(userService.getUser(any(), any()))
                .thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());

        verify(userService, times(1)).getUser(any(), any());
    }
}
