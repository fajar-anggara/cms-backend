package com.backendapp.cms.users.dto;

import com.backendapp.cms.security.validation.annotation.UniqueEmail;
import com.backendapp.cms.security.validation.annotation.UniqueUser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class UserUpdateDto {
    @UniqueUser
    private Optional<String> username = Optional.empty();

    private Optional<String> displayName = Optional.empty();

    @UniqueEmail
    private Optional<String> email = Optional.empty();

    private Optional<String> bio = Optional.empty();

    private Optional<String> profilePicture = Optional.empty();

    private Optional<Boolean> enabled = Optional.empty();

    private Optional<Boolean> isEmailVerified = Optional.empty();

}