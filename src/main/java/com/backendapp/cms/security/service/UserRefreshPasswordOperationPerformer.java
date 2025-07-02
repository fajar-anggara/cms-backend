package com.backendapp.cms.security.service;

import com.backendapp.cms.common.constant.AppConstants;
import com.backendapp.cms.email.service.EmailService;
import com.backendapp.cms.openapi.dto.RenewPasswordRequest;
import com.backendapp.cms.security.exception.PasswordMismatchException;
import com.backendapp.cms.security.entity.RefreshPasswordTokenEntity;
import com.backendapp.cms.security.validation.PasswordMatchValidator;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.security.exception.ExpiredRefreshPasswordTokenException;
import com.backendapp.cms.users.exception.UsernameOrEmailNotFoundException;
import com.backendapp.cms.users.exception.WrongPasswordRefreshToken;
import com.backendapp.cms.users.repository.RefreshPasswordTokenCrudRepository;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class UserRefreshPasswordOperationPerformer {

    private final EmailService emailService;
    private static final Random random = new Random();
    private final UserCrudRepository userCrudRepository;
    private final RefreshPasswordTokenCrudRepository refreshPasswordTokenCrudRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppConstants appConstants;
    private final UserEntityProvider userEntityProvider;
    private final PasswordMatchValidator passwordMatchValidator;

    @Transactional
    public void sendRefreshPasswordTokenTo(String identifier, String generatedToken) {
        UserEntity user = userEntityProvider.getUser(identifier);
        RefreshPasswordTokenEntity refreshPasswordToken = setRefreshPasswordToken(user, generatedToken);
        String username = refreshPasswordToken.getUser().getUsername();
        String token = refreshPasswordToken.getToken();

        String subject = "Permintaan reset password";
        String message = "<html><body>"
                + "<p>Halo " + username + ",</p>"
                + "<p>Kami menerima permintaan untuk mereset password akun Anda.</p>"
                + "<p>Silakan masukan code \"" + token + "\" ke URL berikut:</p>"
                + "<p><a href=\"" + appConstants.REFRESH_PASSWORD_TOKEN_URL + "\">Reset Password Anda</a></p>"
                + "<p>Link ini akan kadaluarsa dalam \"" + appConstants.REFRESH_PASSWORD_TOKEN_EXPIRED_TIME_IN_MINUTES + "\" menit.</p>"
                + "<p>Jika Anda tidak meminta ini, abaikan email ini.</p>"
                + "<p>Salam hangat,<br>Tim CMS</p>"
                + "</body></html>";

        log.info("Send email with user {}, refresh password token {}, and expired token {} minutes.", username, token, appConstants.REFRESH_PASSWORD_TOKEN_EXPIRED_TIME_IN_MINUTES);
        emailService.sendHttpEmail(refreshPasswordToken.getUser().getEmail(), subject, message);
    }

    @Transactional
    public UserEntity verifiedUserRefreshPassword(RenewPasswordRequest request) {
        RefreshPasswordTokenEntity refreshPasswordTokenEntity = refreshPasswordTokenCrudRepository.findByToken(request.getResetPasswordToken())
                .orElseThrow(WrongPasswordRefreshToken::new);
        if (refreshPasswordTokenEntity.isExpired()) {
            throw new ExpiredRefreshPasswordTokenException();
        }
        UserEntity user = refreshPasswordTokenEntity.getUser();
        if (!request.getPassword().equals(passwordEncoder.encode(request.getPassword()))) {
            throw new PasswordMismatchException();
        }
        String password = passwordEncoder.encode(request.getPassword());
        user.setPassword(password);

        return userCrudRepository.save(user);
    }

    public String getRefreshPasswordToken() {
        int number = 1000 + random.nextInt(9000);
        return String.valueOf(number);
    }

    private RefreshPasswordTokenEntity setRefreshPasswordToken(UserEntity user, String generatedToken) {
        LocalDateTime expiredDate = LocalDateTime.now().plusMinutes(5);

        RefreshPasswordTokenEntity refreshPasswordToken = new RefreshPasswordTokenEntity();
        refreshPasswordToken.setUser(user);
        refreshPasswordToken.setToken(generatedToken);
        refreshPasswordToken.setExpDate(expiredDate);

        log.info("Set refresh password token with user {}, token {}, and exp date {}", user, generatedToken, expiredDate);
        return refreshPasswordTokenCrudRepository.save(refreshPasswordToken);
    }
}
