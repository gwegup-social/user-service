package com.ritrovo.userservice.service;

import com.ritrovo.userservice.dao.UserRepository;
import com.ritrovo.userservice.error.RitrovoException;
import com.ritrovo.userservice.model.User;
import com.ritrovo.userservice.util.Constants;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public boolean addUser(User user){
        try {
            userRepository.save(user);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public Boolean updateCorporateDetails(String email, int id){

        String company = driveCompanyNameFromEmail(email);
        userRepository.updateCorporateEmailandCompanyName(id, email, company, (new Date()));
        // invoke verification via auth service ----
        return true;
    }

    public boolean deActivateUser(int id){
        userRepository.updateStatus(id, String.valueOf(User.Status.INACTIVE));
        return true;
    }

    public boolean activateUser(int id){
        userRepository.updateStatus(id, String.valueOf(User.Status.ACTIVE));
        return true;
    }

    private String driveCompanyNameFromEmail(String email){
        String name = email.substring(email.indexOf('@')+1, email.indexOf('.'));
        name = StringUtils.capitalize(name);
        return name;
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).get();
    }

    public User getUserByEmailOrPhone(String email, String phone){
        User user = getUserByEmail(email);
        if(user == null){
            user = getUserByPhone(phone);
        }
        return user;
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User getUserByPhone(String phone){
        return userRepository.findByPhone(phone);
    }

    public Boolean validateEmail(String email){
        return EmailValidator.getInstance().isValid(email);
    }

    public Boolean validatePhone(String phone, String locale){

        if(locale == Constants.LOCALE_DEFAULT){
            return phone.matches(Constants.IN_PHONE_REGEX);
        }
        return false;
    }

    public boolean validateUser(User user) throws RitrovoException {
        String phone = user.getPhone();
        String email = user.getEmail();
        if(user == null){
            logger.error("User can't be empty");
            throw new RitrovoException(HttpStatus.BAD_REQUEST, "Request Body is empty");
        }

        validateEmailorPhone(user);

        if(!StringUtils.isEmpty(email) && getUserByEmail(email) != null){
            logger.error("Email already exist");
            throw new RitrovoException(HttpStatus.UNPROCESSABLE_ENTITY, "Email already exist");
        }

        if(!StringUtils.isEmpty(phone) && getUserByPhone(phone) != null){
            logger.error("Phone already exist");
            throw new RitrovoException(HttpStatus.UNPROCESSABLE_ENTITY, "Phone already exist");
        }
        return true;
    }

    public void validateEmailorPhone(User user) throws RitrovoException {

        if(StringUtils.isEmpty(user.getPhone()) && StringUtils.isEmpty(user.getEmail())){
            logger.error("Email or Phone is missing email: {} and phone: {}",user.getPhone(), user.getEmail());
            throw new RitrovoException(HttpStatus.BAD_REQUEST, "Email or Phone is missing");
        }

        if(!StringUtils.isEmpty(user.getPhone()) && !validatePhone(user.getPhone(), Constants.LOCALE_DEFAULT)){
            logger.error("Invalid phone number: {}", user.getPhone());
            throw new RitrovoException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid phone number");
        }

        if(!StringUtils.isEmpty(user.getEmail()) && !validateEmail(user.getEmail())){
            logger.error("Invalid email: {}", user.getEmail());
            throw new RitrovoException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid email");
        }
    }

    public boolean userExists(int id){
        return userRepository.findById(id).isPresent();
    }
}
