package com.backendapp.cms.common.constant;

import org.springframework.beans.factory.annotation.Value;

public class AppConstants {

    @Value("${frontend.url}")
    public static String FRONTEND_URL;

    @Value("${application.refreshPasswordToken.url}")
    public static String REFRESH_PASSWORD_TOKEN_URL;

    @Value("${application.refreshPasswordToken.expTimeInMinutes}")
    public static String REFRESH_PASSWORD_TOKEN_EXPIRED_TIME_IN_MINUTES;
}
