package com.ritrovo.userservice.service;

import com.ritrovo.userservice.model.dto.UserDto;
import com.ritrovo.userservice.model.response.AuthTokenPair;
import com.ritrovo.userservice.model.response.LoginResponse;
import com.ritrovo.userservice.util.AuthenticationUtils;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserService userService;
    private final AuthService authService;

    public LoginService(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    public LoginResponse getLoginResponse() {

        String userId = AuthenticationUtils.getAuthenticatedUserId();
        UserDto userDetails = userService.getUserDetails(userId);
        AuthTokenPair accessTokenPair = authService.getAccessTokenPair(userId);

        return LoginResponse
                .builder()
                .accessToken(accessTokenPair.getAuthToken())
                .displayName(userDetails.getDisplayName())
                .orgName(userDetails.getCompanyName())
                .userId(userId)
                .status(userDetails.getStatus())
                .build();

    }
}
