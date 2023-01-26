package com.ritrovo.userservice.controller;

import com.ritrovo.userservice.error.RitrovoException;
import com.ritrovo.userservice.model.User;
import com.ritrovo.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${x-app-token}")
    private String tokenStored;

    @GetMapping("/findAll")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable(value = "id") Integer id,
                                  @RequestHeader(value = "X-APP-TOKEN") String X_APP_TOKEN) throws Exception {

        validateToken(X_APP_TOKEN);

        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/corporateEmail/{id}")
    public ResponseEntity updateCorporateEmail(@PathVariable(value = "id") Integer id,
                                               @RequestBody User user,
                                               @RequestHeader(value = "X-APP-TOKEN") String X_APP_TOKEN) throws Exception {

        validateToken(X_APP_TOKEN);

        if (userService.validateEmail(user.getCorporateEmail())) {
            userService.updateCorporateDetails(user.getCorporateEmail(), id);
        } else {
            throw new RitrovoException(HttpStatus.UNPROCESSABLE_ENTITY, "Email is invalid");
        }
        return ResponseEntity.ok("Corporate Email updated successfully");
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity activateUser(@PathVariable(value = "id") Integer id,
                                       @RequestHeader(value = "X-APP-TOKEN") String X_APP_TOKEN) throws Exception {

        validateToken(X_APP_TOKEN);

        if (userService.userExists(id)) {
            userService.activateUser(id);
        } else {
            throw new RitrovoException(HttpStatus.BAD_REQUEST, "User doesn't exist");
        }
        return ResponseEntity.ok("User deactivated successfully");
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity deActivateUser(@PathVariable(value = "id") Integer id,
                                         @RequestHeader(value = "X-APP-TOKEN") String X_APP_TOKEN) throws Exception {

        validateToken(X_APP_TOKEN);

        if (userService.userExists(id)) {
            userService.deActivateUser(id);
        } else {
            throw new RitrovoException(HttpStatus.BAD_REQUEST, "User doesn't exist");
        }
        return ResponseEntity.ok("User deactivated successfully");
    }

    @PostMapping(value = "/createUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addNewUser(@RequestBody User user,
                                     @RequestHeader(value = "X-APP-TOKEN") String X_APP_TOKEN) throws Exception {

        validateToken(X_APP_TOKEN);

        userService.validateUser(user);
        userService.addUser(user);
        return ResponseEntity.accepted().body("User created successfully");
    }

    @GetMapping("/search")
    public ResponseEntity searchUser(@RequestBody User user,
                                     @RequestHeader(value = "X-APP-TOKEN") String X_APP_TOKEN) throws Exception {

        validateToken(X_APP_TOKEN);

        User user1 = null;
        if (user.getId() != null) {
            user1 = userService.getUserById(user.getId());
        } else {
            userService.validateEmailorPhone(user);
            user1 = userService.getUserByEmailOrPhone(user.getEmail(), user.getPhone());
        }
        if (user1 == null) {
            throw new RitrovoException(HttpStatus.BAD_REQUEST, "User doesn't exist");
        }
        return ResponseEntity.ok(user1);
    }

    private void validateToken(String token) throws RitrovoException {
        if (StringUtils.isEmpty(token)) {
            throw new RitrovoException(HttpStatus.BAD_REQUEST, "X-APP-TOKEN is mandatory");
        }
        if (!token.equals(tokenStored)) {
            throw new RitrovoException(HttpStatus.BAD_REQUEST, "X-APP-TOKEN is incorrect");
        }
    }

}
