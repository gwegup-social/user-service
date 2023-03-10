package com.ritrovo.userservice.controller;

import com.google.common.base.Preconditions;
import com.ritrovo.userservice.model.dto.UserDto;
import com.ritrovo.userservice.model.request.UpdateCorporateEmailRequest;
import com.ritrovo.userservice.model.request.UpdateUserProfileRequest;
import com.ritrovo.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateProfileInfo(@RequestBody UpdateUserProfileRequest updateUserProfileRequest) {
        UserDto userDto = userService.updateUserProfile(updateUserProfileRequest);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/profile/corporate-email")
    public ResponseEntity<UserDto> updateCorporateEmail(@RequestBody UpdateCorporateEmailRequest updateCorporateEmailRequest) {
        UserDto userDto = userService.updateCorporateEmail(updateCorporateEmailRequest);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/details")
    public ResponseEntity<UserDto> getUserDetails() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        Preconditions.checkNotNull(principal);
        UserDto userDetails = userService.getUserDetails(principal.toString());
        return ResponseEntity.ok(userDetails);
    }
}
