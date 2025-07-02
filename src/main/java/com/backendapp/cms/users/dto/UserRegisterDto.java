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
    private String username;

    private String displayName;

    @UniqueEmail
    private String email;

    private String password;

    private String confirmPassword;

    private Authority authority;

    private Boolean enabled = UserConstants.DEFAULT_ENABLE;

    private Boolean isEmailVerified = UserConstants.DEFAULT_EMAIL_VERIFIED;
}
