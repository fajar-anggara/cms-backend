package com.backendapp.cms.users.dto;

import com.backendapp.cms.common.constant.UserConstants;
import com.backendapp.cms.common.enums.Authority;
import com.backendapp.cms.security.validation.annotation.UniqueEmail;
import com.backendapp.cms.security.validation.annotation.UniqueUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class UserRegisterDto {

    @UniqueUser
    private Optional<String> username = Optional.empty();

    private Optional<String> displayName = Optional.empty();

    @UniqueEmail
    private Optional<String> email = Optional.empty();

    private Optional<String> password = Optional.empty();

    private Optional<String> confirmPassword = Optional.empty();

    private Authority authority;

    private Boolean enabled = UserConstants.DEFAULT_ENABLE;

    private Boolean emailVerified = UserConstants.DEFAULT_EMAIL_VERIFIED;
}
