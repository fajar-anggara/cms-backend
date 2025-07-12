package com.backendapp.cms.blogging.contract;

import com.backendapp.cms.common.enums.Authority;
import com.backendapp.cms.openapi.dto.UserSimpleResponse;
import com.backendapp.cms.security.entity.UserGrantedAuthority;
import com.backendapp.cms.users.entity.UserEntity;

import java.time.LocalDateTime;

public class UserBuilder {
    private Long id;
    private String password;
    private String username;
    private String email;
    private String displayName;
    private String profilePicture;
    private String bio;
    private UserGrantedAuthority authority;
    private boolean enabled;
    private boolean emailVerified;
    private String resetPasswordToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public UserBuilder withDefault() {
        this.id = 1L;
        this.username = "username";
        this.email = "email";
        this.password = "password";
        this.displayName =  "displayName";
        this.bio =  "This is a bio";
        this.authority = new UserGrantedAuthority(2L, Authority.BLOGGER);
        this.profilePicture = "/profile-picture.jpg";
        this.enabled = true;
        this.emailVerified = false;
        this.resetPasswordToken =  null;
        this.createdAt = LocalDateTime.of(2025, 7, 10, 19, 45, 23);
        this.updatedAt =  null;
        this.deletedAt =  null;

        return this;
    }

    public UserBuilder setAuthorityBlogger() {
        this.authority = new UserGrantedAuthority(2L, Authority.BLOGGER);
        return this;
    }

    public UserBuilder setAuthorityAdmin() {
        this.authority = new UserGrantedAuthority(1L, Authority.ADMIN);
        return this;
    }

    public UserBuilder setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public UserBuilder serDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt != null ? deletedAt : LocalDateTime.of(2025, 7, 10, 19, 45, 23);;
        return this;
    }

    public UserEntity build() {
        return UserEntity.builder()
                .id(id)
                .username(username)
                .displayName(displayName)
                .email(email)
                .password(password)
                .profilePicture(profilePicture)
                .bio(bio)
                .authority(authority)
                .enabled(enabled)
                .emailVerified(emailVerified)
                .resetPasswordToken(resetPasswordToken)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }

    public UserSimpleResponse buildUserSimpleResponse() {
        UserSimpleResponse userSimpleResponse = new UserSimpleResponse();
        userSimpleResponse.setId(id);
        userSimpleResponse.setUsername(username);
        userSimpleResponse.setDisplayName(displayName);
        userSimpleResponse.setProfilePicture(profilePicture);
        userSimpleResponse.setBio(bio);
        userSimpleResponse.setAuthority(UserSimpleResponse.AuthorityEnum.valueOf(authority.getAuthority().substring(5)));
        userSimpleResponse.setEnabled(enabled);

        return userSimpleResponse;
    }
}
