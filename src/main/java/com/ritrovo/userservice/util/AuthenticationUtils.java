package com.ritrovo.userservice.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class AuthenticationUtils {

    public static String getAuthenticatedUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        User authenticatedUser = (User) principal;
        return authenticatedUser.getUsername();
    }
}
