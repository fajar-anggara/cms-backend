package com.backendapp.cms.common.constant;

import com.backendapp.cms.common.enums.Roles;
import com.backendapp.cms.security.entity.UserGrantedAuthority;

public final class UserConstants {

    private UserConstants() {}

    public static final String DEFAULT_PROFILE_PICTURE = "/default_avatar";
    public static final String DEFAULT_BIO = "";
    public static final UserGrantedAuthority DEFAULT_ROLE = UserGrantedAuthority.builder().role(Roles.USER).build();
    public static final boolean DEFAULT_ENABLE = true;
    public static final boolean DEFAULT_EMAIL_VERIFIED = false;

}
