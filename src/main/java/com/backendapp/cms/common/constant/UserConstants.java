package com.backendapp.cms.common.constant;

import com.backendapp.cms.common.enums.Authority;
import com.backendapp.cms.security.repository.UserGrantedAuthorityRepository;

public final class UserConstants {
    private UserGrantedAuthorityRepository userGrantedAuthority;

    private UserConstants(
            UserGrantedAuthorityRepository userGrantedAuthority
    ) {
        this.userGrantedAuthority = userGrantedAuthority;
    }

    public static final String DEFAULT_PROFILE_PICTURE = "/default_avatar";
    public static final String DEFAULT_BIO = "";
    public static final Authority DEFAULT_ROLE = Authority.BLOGGER;
    public static final boolean DEFAULT_ENABLE = true;
    public static final boolean DEFAULT_EMAIL_VERIFIED = false;

}
