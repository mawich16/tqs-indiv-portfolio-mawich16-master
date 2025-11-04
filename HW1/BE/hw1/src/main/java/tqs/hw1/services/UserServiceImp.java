package tqs.hw1.services;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import tqs.hw1.data.User;
import tqs.hw1.data.UserRepository;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String name, String password, boolean isStaff) {
        if (userRepository.existsByUsername(name)) {
            throw new IllegalArgumentException("Username already in use");
        }
        User user = new User(name, password, isStaff);
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(String name, String password) {
        return userRepository.findByUsernameAndPassword(name, password)
                .orElseThrow(() -> new EntityNotFoundException("User not found with name and provided password " ));
    }
}
