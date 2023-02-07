package com.ritrovo.userservice.controller;

import com.ritrovo.userservice.model.dto.UserDto;
import com.ritrovo.userservice.model.request.UpdateUserProfileRequest;
import com.ritrovo.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
