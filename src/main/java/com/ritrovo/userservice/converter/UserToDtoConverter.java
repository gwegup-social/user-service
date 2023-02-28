package com.ritrovo.userservice.converter;

import com.ritrovo.userservice.entity.User;
import com.ritrovo.userservice.model.dto.UserDto;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.util.Objects;

public class UserToDtoConverter implements Converter<User, UserDto> {

    @Override
    public UserDto convert(User source) {

        LocalDate dateOfBirth = source.getDateOfBirth();
        User.Gender gender = source.getGender();

        return UserDto
                .builder()
                .userId(source.getUserId())
                .displayName(source.getDisplayName())
                .companyName(source.getCompanyName())
                .gender(Objects.nonNull(gender) ? gender.getGender() : "")
                .dateOfBirth((Objects.nonNull(dateOfBirth) ? dateOfBirth.toString() : ""))
                .personalEmail(source.getPersonalEmail())
                .corporateEmail(source.getCorporateEmail())
                .build();
    }
}
