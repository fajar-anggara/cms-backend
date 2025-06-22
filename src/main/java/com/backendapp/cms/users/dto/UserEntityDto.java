package com.backendapp.cms.users.dto;

import com.backendapp.cms.security.entity.UserGrantedAuthority;
import lombok.Value;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;

/**
 * DTO for {@link com.backendapp.cms.users.entity.UserEntity}
 */
@Value
public class UserEntityDto implements Serializable {
    Long id;
    String username;
    String email;
    String profilePicture;
    String bio;
    Set<UserGrantedAuthority> authorities;
    boolean isEnabled;
    boolean isEmailVerified;
    String resetPasswordToken;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}