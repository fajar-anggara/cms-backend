package com.backendapp.cms.users.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class UserUpdateDto {
    private Optional<String> username = Optional.empty();
    private Optional<String> displayName = Optional.empty();
    private Optional<String> email = Optional.empty();
    private Optional<String> bio = Optional.empty();
    private Optional<String> profilePicture = Optional.empty();
    private Optional<Boolean> enabled = Optional.empty();
    private Optional<Boolean> isEmailVerified = Optional.empty();
}