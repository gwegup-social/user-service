package com.ritrovo.userservice.service;

import com.google.common.base.Preconditions;
import com.ritrovo.userservice.entity.User;
import com.ritrovo.userservice.error.UserOnboardingException;
import com.ritrovo.userservice.handler.UserHandler;
import com.ritrovo.userservice.model.dto.UserDto;
import com.ritrovo.userservice.model.request.EmailRegistrationRequest;
import com.ritrovo.userservice.model.request.UpdateUserProfileRequest;
import com.ritrovo.userservice.model.response.AuthTokenPair;
import com.ritrovo.userservice.model.response.LoginResponse;
import com.ritrovo.userservice.model.response.OtpStatusResponse;
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
import java.util.Locale;
import java.util.Objects;
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

    public LoginResponse onboardUser(EmailRegistrationRequest emailRegistrationRequest) {
        performDataPreprocessing(emailRegistrationRequest);
        performValidationChecks(emailRegistrationRequest);

        String email = emailRegistrationRequest.getEmail();
        User onboardedUser = userHandler.onboardUserUsingPersonalEmailId(email);
        String userId = onboardedUser.getUserId();
        authService.initialiseUserCredentials(userId, email, emailRegistrationRequest.getPassword());
        AuthTokenPair accessTokenPair = authService.getAccessTokenPair(userId);

        return LoginResponse
                .builder()
                .userId(userId)
                .displayName(onboardedUser.getDisplayName())
                .orgName(onboardedUser.getCompanyName())
                .status(onboardedUser.getStatus().getValue())
                .accessToken(accessTokenPair.getAuthToken())
                .refreshToken(accessTokenPair.getRefreshToken())
                .build();
    }

    private void performDataPreprocessing(EmailRegistrationRequest emailRegistrationRequest) {

        if (Objects.isNull(emailRegistrationRequest))
            return;

        String email = emailRegistrationRequest.getEmail();
        if (StringUtils.isNotBlank(email)) {
            email = StringUtils.deleteWhitespace(email);
            email = email.toLowerCase();
            emailRegistrationRequest.setEmail(email);
        }
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

        corporateEmail = corporateEmail.toLowerCase(Locale.ROOT);
        user.setCorporateEmail(corporateEmail);
        String companyName = getCompanyNameFromCorporateEmail(corporateEmail);
        user.setCompanyName(companyName);
        user.setStatus(User.Status.CORPORATE_EMAIL_VERIFIED);

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

        validateEmailForExistingUsers(emailRegistrationRequest.getEmail());
        validateEmailOtpVerification(emailRegistrationRequest.getOtpRequestId());
    }

    private void validateEmailOtpVerification(String otpRequestId) {
        OtpStatusResponse otpRequestStatus = authService.getOtpRequestStatus(otpRequestId);

        if (Objects.isNull(otpRequestStatus)) {
            throw new RuntimeException("email not yet verified");
        }

        boolean isVerified = otpRequestStatus.getOtpVerificationStatus().equalsIgnoreCase("VERIFICATION_COMPLETED");

        if (!isVerified)
            throw new RuntimeException("email not yet verified");
    }

    private void validateEmailForExistingUsers(String email) {
        List<User> existingUsers = userHandler.findUserByEmail(email);
        if (!existingUsers.isEmpty())
            throw new UserOnboardingException(HttpStatus.INTERNAL_SERVER_ERROR, "multiple users found with same email id");
    }

    public UserDto getUserDetails(String userId) {
        Optional<User> userOptional = userHandler.findUserById(userId);
        User user = userOptional.orElseThrow(() -> new RuntimeException("user not found"));
        return conversionService.convert(user, UserDto.class);
    }
}
