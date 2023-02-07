package com.ritrovo.userservice.handler;

import com.ritrovo.userservice.dao.UserRepository;
import com.ritrovo.userservice.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserHandler {

    private final UserRepository userRepository;

    public UserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findUserByEmail(String emailId) {
        return userRepository.findUserByEmail(emailId);
    }

    public User onboardUserUsingPersonalEmailId(String email) {

        User newUser = User
                .builder()
                .personalEmail(email)
                .firstName("User")
                .status(User.Status.PERSONAL_EMAIL_VERIFICATION_PENDING)
                .build();

        return saveUser(newUser);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUserById(String userId) {

        return userRepository.findById(userId);
    }
}
