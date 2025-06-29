package com.backendapp.cms.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstants {

    @Value("${frontend.url}")
    public String FRONTEND_URL;

    @Value("${application.refreshPasswordToken.url}")
    public String REFRESH_PASSWORD_TOKEN_URL;

    @Value("${application.refreshPasswordToken.expTimeInMinutes}")
    public String REFRESH_PASSWORD_TOKEN_EXPIRED_TIME_IN_MINUTES;
}
