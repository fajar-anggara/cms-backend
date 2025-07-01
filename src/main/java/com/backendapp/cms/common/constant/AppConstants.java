package com.backendapp.cms.common.constant;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import jakarta.annotation.PostConstruct;

@Component
public class AppConstants {

    @Value("${application.frontend.url}")
    public String FRONTEND_URL;

    @Value("${application.refreshPasswordToken.url}")
    public String REFRESH_PASSWORD_TOKEN_URL;

    @Value("${application.refreshPasswordToken.expired-times-in-minutes}")
    public String REFRESH_PASSWORD_TOKEN_EXPIRED_TIME_IN_MINUTES;

    @Value("${application.security.jwt.access-secret-key}")
    private String ACCESS_KEY_STRING;

    @Value("${application.security.jwt.refresh-secret-key}")
    private String REFRESH_KEY_STRING;

    public SecretKey JWT_ACCESS_TOKEN_SECRET_KEY;
    public SecretKey JWT_REFRESH_TOKEN_SECRET_KEY;

    @Value("${application.security.jwt.access-token-expired-times-in-millisecond}")
    public Long JWT_ACCESS_TOKEN_EXPIRED;

    @Value("${application.security.jwt.refresh-token-expired-times-in-millisecond}")
    public Long JWT_REFRESH_TOKEN_EXPIRED;

    @PostConstruct
    public void init() {
        this.JWT_ACCESS_TOKEN_SECRET_KEY = getKey(ACCESS_KEY_STRING);
        this.JWT_REFRESH_TOKEN_SECRET_KEY = getKey(REFRESH_KEY_STRING);
    }

    // SUPERUSER INITIALIZER

    @Value("${superuser.username}")
    public String SUPERUSER_USERNAME;

    @Value("${superuser.display-name}")
    public String SUPERUSER_DISPLAY_NAME;

    @Value("${superuser.email}")
    public String SUPERUSER_EMAIL;

    @Value("${superuser.password}")
    public String SUPERUSER_PASSWORD;

    private SecretKey getKey(String secretKey) {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT secret key tidak boleh kosong");
        }
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}