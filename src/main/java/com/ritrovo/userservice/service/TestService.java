package com.ritrovo.userservice.service;

import com.ritrovo.userservice.dao.UserRepository;
import com.ritrovo.userservice.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class TestService implements CommandLineRunner {
    private final UserRepository userRepository;

    public TestService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

/*        User user = User
                .builder()
                .firstName("Amit")
                .lastName("Gupta")
                .companyName("Morgan Stanley")
                .corporateEmail("amit.gupta@morganstanley.com")
                .dateOfBirth(LocalDate.of(1992, 1, 1))
                .gender(User.Gender.MALE)
                .personalEmail("amit.gupta@gmail.com")
                .loginDisabled(false)
                .phone("9826018227")
                .status(User.Status.PERSONAL_EMAIL_VERIFICATION_PENDING)
                .build();

        List<User> byPersonalEmailOrCorporateEmail = userRepository.findUserByEmail("amit.gupta@gmail.com");
        List<User> byPersonalEmailOrCorporateEmail2 = userRepository.findUserByEmail("amit.gupta@morganstanley.com");*/
    }
}
