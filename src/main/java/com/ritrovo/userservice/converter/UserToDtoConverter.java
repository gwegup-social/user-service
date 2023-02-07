package com.ritrovo.userservice.converter;

import com.ritrovo.userservice.entity.User;
import com.ritrovo.userservice.model.dto.UserDto;
import org.springframework.core.convert.converter.Converter;

public class UserToDtoConverter implements Converter<User, UserDto> {

    @Override
    public UserDto convert(User source) {

        return UserDto
                .builder()
                .userId(source.getUserId())
                .displayName(source.getFirstName() + " " + source.getLastName())
                .companyName(source.getCompanyName())
                .gender(source.getGender().getGender())
                .dateOfBirth(source.getDateOfBirth().toString())
                .personalEmail(source.getPersonalEmail())
                .corporateEmail(source.getCorporateEmail())
                .build();
    }
}
