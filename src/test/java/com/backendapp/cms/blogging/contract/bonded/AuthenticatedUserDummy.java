package com.backendapp.cms.blogging.contract.bonded;

import com.backendapp.cms.common.enums.Authority;
import com.backendapp.cms.openapi.dto.UserSimpleResponse;
import com.backendapp.cms.security.entity.UserGrantedAuthority;
import com.backendapp.cms.users.entity.UserEntity;

import java.time.LocalDateTime;

public class AuthenticatedUserDummy {
    // common data
    private static final String password = "encodedPassword";
    private static final String profilePicture = "/avatar";
    private static final String bio = "user set its bio";
    private static final boolean enabled = true;
    private static final boolean emailVerified = true;
    private static final String resetPasswordToken = null;
    private static final LocalDateTime createdAt = LocalDateTime.now();
    private static final LocalDateTime updatedAt = null;
    private static final LocalDateTime deletedAt = null;

    public static final UserEntity BLOGGER_USER;
    public static final UserSimpleResponse USER_SIMPLE_RESPONSE_BLOGGER;
    public static final UserEntity ADMIN_USER;
    public static final UserEntity DELETED_USER;
    public static final UserEntity DISABLED_USER;

    static {
        BLOGGER_USER = UserEntity.builder()
                .id(1L)
                .username("blogger")
                .displayName("blogger")
                .email("blogger@example.com")
                .password(password)
                .profilePicture(profilePicture)
                .bio(bio)
                .authority(new UserGrantedAuthority(2L, Authority.BLOGGER))
                .enabled(enabled)
                .emailVerified(emailVerified)
                .resetPasswordToken(resetPasswordToken)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();

        USER_SIMPLE_RESPONSE_BLOGGER = new UserSimpleResponse();
        USER_SIMPLE_RESPONSE_BLOGGER.setId(BLOGGER_USER.getId());
        USER_SIMPLE_RESPONSE_BLOGGER.setUsername(BLOGGER_USER.getUsername());
        USER_SIMPLE_RESPONSE_BLOGGER.setDisplayName(BLOGGER_USER.getDisplayName());
        USER_SIMPLE_RESPONSE_BLOGGER.setBio(BLOGGER_USER.getBio());
        USER_SIMPLE_RESPONSE_BLOGGER.setAuthority(UserSimpleResponse.AuthorityEnum.BLOGGER);
        USER_SIMPLE_RESPONSE_BLOGGER.setEnabled(BLOGGER_USER.isEnabled());
        USER_SIMPLE_RESPONSE_BLOGGER.setProfilePicture(BLOGGER_USER.getProfilePicture());

        ADMIN_USER = UserEntity.builder()
                .id(2L)
                .username("superuser")
                .displayName("superuser")
                .email("supseruser@example.com")
                .password(password)
                .profilePicture(profilePicture)
                .bio(bio)
                .authority(new UserGrantedAuthority(2L, Authority.ADMIN))
                .enabled(enabled)
                .emailVerified(emailVerified)
                .resetPasswordToken(resetPasswordToken)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();

        DELETED_USER = UserEntity.builder()
                .id(3L)
                .username("deleted")
                .displayName("deleted")
                .email("deleted@example.com")
                .password(password)
                .profilePicture(profilePicture)
                .bio(bio)
                .authority(new UserGrantedAuthority(2L, Authority.BLOGGER))
                .enabled(false)
                .emailVerified(emailVerified)
                .resetPasswordToken(resetPasswordToken)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(LocalDateTime.now())
                .build();

        DISABLED_USER = UserEntity.builder()
                .id(4L)
                .username("deleted")
                .displayName("deleted")
                .email("deleted@example.com")
                .password(password)
                .profilePicture(profilePicture)
                .bio(bio)
                .authority(new UserGrantedAuthority(2L, Authority.BLOGGER))
                .enabled(false)
                .emailVerified(emailVerified)
                .resetPasswordToken(resetPasswordToken)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }

    public static UserEntity createCustomUser(Long id, String username, String email, UserGrantedAuthority authority) {
        return UserEntity.builder()
                .id(id)
                .username(username)
                .displayName(username)
                .email(email)
                .password(password)
                .profilePicture(profilePicture)
                .bio(bio)
                .authority(authority)
                .enabled(enabled)
                .emailVerified(emailVerified)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }
}
