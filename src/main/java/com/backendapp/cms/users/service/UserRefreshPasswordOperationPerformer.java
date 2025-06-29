package com.backendapp.cms.users.service;

import com.backendapp.cms.common.constant.AppConstants;
import com.backendapp.cms.email.service.EmailService;
import com.backendapp.cms.openapi.dto.RenewPasswordRequest;
import com.backendapp.cms.security.exception.PasswordMismatchException;
import com.backendapp.cms.users.entity.RefreshPasswordTokenEntity;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.exception.ExpiredRefreshPasswordTokenException;
import com.backendapp.cms.users.exception.UsernameOrEmailNotFoundException;
import com.backendapp.cms.users.exception.WrongPasswordRefreshToken;
import com.backendapp.cms.users.repository.RefreshPasswordTokenCrudRepository;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
public class UserRefreshPasswordOperationPerformer {

    private final EmailService emailService;
    private static final Random random = new Random();
    private final UserCrudRepository userCrudRepository;
    private final RefreshPasswordTokenCrudRepository refreshPasswordTokenCrudRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppConstants appConstants;

    @Transactional
    public void sendRefreshPasswordTokenTo(String identifier, String generatedToken) {
        UserEntity user = verifiedUser(identifier);
        RefreshPasswordTokenEntity refreshPasswordToken = setRefreshPasswordToken(user, generatedToken);

        String subject = "Permintaan reset password";
        String message = "<html><body>"
                + "<p>Halo " + refreshPasswordToken.getUser().getUsername() + ",</p>"
                + "<p>Kami menerima permintaan untuk mereset password akun Anda.</p>"
                + "<p>Silakan masukan code \"" + refreshPasswordToken.getToken() + "\" ke URL berikut:</p>"
                + "<p><a href=\"" + appConstants.REFRESH_PASSWORD_TOKEN_URL + "\">Reset Password Anda</a></p>"
                + "<p>Link ini akan kadaluarsa dalam \"" + appConstants.REFRESH_PASSWORD_TOKEN_EXPIRED_TIME_IN_MINUTES + "\" menit.</p>"
                + "<p>Jika Anda tidak meminta ini, abaikan email ini.</p>"
                + "<p>Salam hangat,<br>Tim CMS</p>"
                + "</body></html>";

        emailService.sendHttpEmail(refreshPasswordToken.getUser().getEmail(), subject, message);
    }

    @Transactional
    public UserEntity verifiedUserRefreshPassword(@Valid @NotNull RenewPasswordRequest request) {
        RefreshPasswordTokenEntity refreshPasswordTokenEntity = refreshPasswordTokenCrudRepository.findByToken(request.getResetPasswordToken())
                .orElseThrow(WrongPasswordRefreshToken::new);
        if (refreshPasswordTokenEntity.isExpired()) {
            throw new ExpiredRefreshPasswordTokenException();
        }
        UserEntity user = refreshPasswordTokenEntity.getUser();
        if (!request.getPassword().equals(request.getConfirmPassword())) {
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

    private UserEntity verifiedUser(String identifier) {
        return userCrudRepository.findByUsername(identifier)
                .orElseGet(() -> userCrudRepository.findByEmail(identifier)
                    .orElseThrow(UsernameOrEmailNotFoundException::new));
    }

    private RefreshPasswordTokenEntity setRefreshPasswordToken(UserEntity user, String generatedToken) {
        LocalDateTime expiredDate = LocalDateTime.now().plusMinutes(5);

        RefreshPasswordTokenEntity refreshPasswordToken = new RefreshPasswordTokenEntity();
        refreshPasswordToken.setUser(user);
        refreshPasswordToken.setToken(generatedToken);
        refreshPasswordToken.setExpDate(expiredDate);

        return refreshPasswordTokenCrudRepository.save(refreshPasswordToken);
    }
}
