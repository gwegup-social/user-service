package com.ritrovo.userservice.service;

import com.google.common.base.Preconditions;
import com.ritrovo.userservice.entity.User;
import com.ritrovo.userservice.error.UserOnboardingException;
import com.ritrovo.userservice.handler.UserHandler;
import com.ritrovo.userservice.model.dto.UserDto;
import com.ritrovo.userservice.model.request.EmailRegistrationRequest;
import com.ritrovo.userservice.model.request.UpdateUserProfileRequest;
import com.ritrovo.userservice.model.response.EmailRegistrationResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserHandler userHandler;
    private final AuthService authService;
    private final ConversionService conversionService;

    public UserService(UserHandler userHandler,
                       AuthService authService,
                       @Qualifier("conversionService") ConversionService conversionService) {
        this.userHandler = userHandler;
        this.authService = authService;
        this.conversionService = conversionService;
    }

    public EmailRegistrationResponse onboardUser(EmailRegistrationRequest emailRegistrationRequest) {
        performValidationChecks(emailRegistrationRequest);

        String email = emailRegistrationRequest.getEmail();
        User onboardedUser = userHandler.onboardUserUsingPersonalEmailId(email);
        boolean isCredentialSaved = authService.initialiseUserCredentials(onboardedUser.getUserId(), email, emailRegistrationRequest.getPassword());

        if (isCredentialSaved) {

            authService.sendOtpOverEmail(email);
            return EmailRegistrationResponse.builder().userId(onboardedUser.getUserId()).status(onboardedUser.getStatus().getValue()).build();
        }

        throw new UserOnboardingException(HttpStatus.INTERNAL_SERVER_ERROR, "unable to save user credentials");
    }

    public UserDto updateUserProfile(UpdateUserProfileRequest updateUserProfileRequest) {

        Preconditions.checkNotNull(updateUserProfileRequest, "update user profile request cant be null");

        String userId = updateUserProfileRequest.getUserId();
        Preconditions.checkNotNull(userId, "userId, userId cant be empty or null to update profile info");

        Optional<User> userOptional = userHandler.findUserById(userId);

        Preconditions.checkArgument(userOptional.isPresent(), "no user found with userid : " + userId);

        User user = userOptional.get();

        String name = updateUserProfileRequest.getName();

        updateName(user, name);
        updateGender(user, updateUserProfileRequest.getGender());
        updateDateOfBirth(user, updateUserProfileRequest.getDateOfBirth());
        updateCorporateEmail(user, updateUserProfileRequest.getCorporateEmail());

        User updatedUser = userHandler.saveUser(user);
        return conversionService.convert(updatedUser, UserDto.class);

    }

    private void updateCorporateEmail(User user, String corporateEmail) {

        Preconditions.checkNotNull(user, "user cant be null to update corporate email");

        if (StringUtils.isBlank(corporateEmail))
            return;

        user.setCorporateEmail(corporateEmail);
        String companyName = getCompanyNameFromCorporateEmail(corporateEmail);
        user.setCompanyName(companyName);
        user.setStatus(User.Status.CORPORATE_EMAIL_VERIFICATION_PENDING);

        authService.sendOtpOverEmail(corporateEmail);
    }

    private String getCompanyNameFromCorporateEmail(String corporateEmail) {

        if (StringUtils.isBlank(corporateEmail))
            return corporateEmail;

        String[] tokens = corporateEmail.split("@");

        if (tokens.length < 2)
            throw new IllegalArgumentException("invalid email id passed");

        String emailSuffix = tokens[1];
        int indexOfDot = emailSuffix.indexOf(".");
        String companyName = emailSuffix.substring(0, indexOfDot);
        return companyName;
    }

    private void updateDateOfBirth(User user, String dateOfBirth) {

        if (StringUtils.isBlank(dateOfBirth)) return;

        LocalDate dob = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        user.setDateOfBirth(dob);
    }

    private void updateGender(User user, String gender) {
        if (StringUtils.isBlank(gender)) return;

        User.Gender genderValue = User.Gender.valueOf(gender.toUpperCase());
        user.setGender(genderValue);
    }

    private void updateName(User user, String name) {
        Preconditions.checkNotNull(user, "user cant be null to update username");

        if (StringUtils.isBlank(name))
            return;

        String[] tokens = StringUtils.splitPreserveAllTokens(name, " ");

        String firstName = tokens[0];
        user.setFirstName(firstName);

        if (tokens.length > 1) {
            StringBuilder lastName = new StringBuilder();
            for (int i = 1; i < tokens.length; i++) {
                lastName.append(tokens[i]);
                lastName.append(" ");
            }
            user.setLastName(lastName.toString());
        }

    }

    private void performValidationChecks(EmailRegistrationRequest emailRegistrationRequest) {

        String email = emailRegistrationRequest.getEmail();
        List<User> existingUsers = userHandler.findUserByEmail(email);

        if (existingUsers.size() > 1)
            throw new UserOnboardingException(HttpStatus.INTERNAL_SERVER_ERROR, "multiple users found with same email id");

        if (CollectionUtils.isNotEmpty(existingUsers)) {

            User existingUser = existingUsers.get(0);
            if (!existingUser.getStatus().equals(User.Status.PERSONAL_EMAIL_VERIFICATION_PENDING))
                throw new UserOnboardingException(HttpStatus.INTERNAL_SERVER_ERROR, "this email id has already been used by another user");
        }

    }
}
