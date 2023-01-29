package com.ritrovo.userservice.service;

import com.ritrovo.userservice.entity.User;
import com.ritrovo.userservice.error.UserOnboardingException;
import com.ritrovo.userservice.handler.UserHandler;
import com.ritrovo.userservice.model.EmailRegistrationResponse;
import com.ritrovo.userservice.model.EmailRegistrationRequest;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserHandler userHandler;
    private final AuthService authService;

    public UserService(UserHandler userHandler, AuthService authService) {
        this.userHandler = userHandler;
        this.authService = authService;
    }

    public EmailRegistrationResponse onboardUser(EmailRegistrationRequest emailRegistrationRequest) {
        performValidationChecks(emailRegistrationRequest);

        String email = emailRegistrationRequest.getEmail();
        User onboardedUser = userHandler.onboardUserUsingPersonalEmailId(email);
        boolean isCredentialSaved = authService.initialiseUserCredentials(onboardedUser.getUserId(), email, emailRegistrationRequest.getPassword());

        if (isCredentialSaved) {
            return EmailRegistrationResponse
                    .builder()
                    .userId(onboardedUser.getUserId())
                    .status(onboardedUser.getStatus().getValue())
                    .build();
        }

        throw new UserOnboardingException(HttpStatus.INTERNAL_SERVER_ERROR, "unable to save user credentials");
    }

    private void performValidationChecks(EmailRegistrationRequest emailRegistrationRequest) {

        String email = emailRegistrationRequest.getEmail();
        List<User> existingUser = userHandler.findUserByEmail(email);

        if (CollectionUtils.isNotEmpty(existingUser)) {
            throw new UserOnboardingException(HttpStatus.BAD_REQUEST, "this email id has already been used by another user");
        }
    }
}
