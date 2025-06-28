package com.backendapp.cms.users.service;

import com.backendapp.cms.common.constant.AppConstants;
import com.backendapp.cms.email.service.EmailService;
import com.backendapp.cms.users.entity.RefreshPasswordTokenEntity;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.exception.UsernameOrEmailNotFoundException;
import com.backendapp.cms.users.repository.RefreshPasswordTokenCrudRepository;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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

    public String getRefreshPasswordToken() {
        int number = 1000 + random.nextInt(9000);
        return String.valueOf(number);
    }

    @Transactional
    public void sendRefreshPasswordTokenTo(String identifier, String generatedToken) {
        UserEntity user = verifiedUser(identifier);
        RefreshPasswordTokenEntity refreshPasswordToken = setRefreshPasswordToken(user, generatedToken);

        String subject = "Permintaan resep password";
        String message = "<html><body>"
                + "<p>Halo " + refreshPasswordToken.getUser().getUsername() + ",</p>"
                + "<p>Kami menerima permintaan untuk mereset password akun Anda.</p>"
                + "<p>Silakan masukan code \"" + refreshPasswordToken.getToken() + "\" ke URL berikut:</p>"
                + "<p><a href=\"" + AppConstants.REFRESH_PASSWORD_TOKEN_URL + "\">Reset Password Anda</a></p>"
                + "<p>Link ini akan kadaluarsa dalam \"" + AppConstants.REFRESH_PASSWORD_TOKEN_EXPIRED_TIME_IN_MINUTES + "\" menit.</p>"
                + "<p>Jika Anda tidak meminta ini, abaikan email ini.</p>"
                + "<p>Salam hangat,<br>Tim CMS</p>"
                + "</body></html>";

        emailService.sendHttpEmail(refreshPasswordToken.getUser().getEmail(), subject, message);
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
