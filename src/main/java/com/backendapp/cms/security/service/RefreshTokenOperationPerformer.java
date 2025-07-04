package com.backendapp.cms.security.service;

import com.backendapp.cms.security.dto.AuthenticationResponse;
import com.backendapp.cms.security.entity.RefreshTokenEntity;
import com.backendapp.cms.security.exception.*;
import com.backendapp.cms.security.jwt.JwtService;
import com.backendapp.cms.security.repository.RefreshTokenRepository;
import com.backendapp.cms.users.entity.UserEntity; // Assuming this is your UserDetails implementation
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException; // For catching expired token during parsing
import io.jsonwebtoken.security.SignatureException; // For catching invalid signature
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional; // Penting untuk operasi database

@Service
@AllArgsConstructor
@Slf4j
public class RefreshTokenOperationPerformer {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public UserEntity validateRefreshAndAccessToken(String accessToken, String refreshToken) {

        // Refresh token check
        String refreshTokenJti;
        try {
            refreshTokenJti = jwtService.extractClaimFromRefresh(refreshToken, Claims::getId);
        } catch (SignatureException e) {
            log.warn("Invalid refresh token signature: {}", e.getMessage());
            throw new InvalidRefreshTokenException();
        } catch (ExpiredJwtException e) {
            log.warn("Provided refresh token is expired during parsing: {}", e.getMessage());
            throw new RefreshTokenExpiredException();
        } catch (Exception e) {
            log.warn("Malformed or invalid refresh token: {}", e.getMessage());
            throw new InvalidRefreshTokenException();
        }

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findById(refreshTokenJti)
                .orElseThrow(RefreshTokenNotFoundException::new);
        log.info("Refresh token id in database: {}", refreshTokenEntity.getId());

        // Get username from both and check it
        String usernameFromAccess;
        try {
            usernameFromAccess = jwtService.extractUsernameFromAccessIgnoreExpired(accessToken);
        } catch (SignatureException e) {
            log.warn("Invalid access token signature: {}", e.getMessage());
            throw new InvalidAccessTokenException();
        } catch (Exception e) {
            log.warn("Malformed or invalid access token: {}", e.getMessage());
            throw new InvalidAccessTokenException();
        }
        String usernameFromRefresh = jwtService.extractUsernameFromRefresh(refreshToken);
        if (!usernameFromAccess.equals(usernameFromRefresh)) {
            log.warn("Username mismatch between access token ({}) and refresh token ({})", usernameFromAccess, usernameFromRefresh);
            throw new AccessAndRefreshTokenMismatchException();
        }


        // if (jwtService.isRefreshTokenExpired(refreshToken)) {
        //     log.warn("Refresh token is expired according to internal check: {}", refreshTokenJti);
        //     throw new RefreshTokenExpiredException("Refresh token has expired.");
        // }

        UserEntity user = refreshTokenEntity.getUser();
        if (user == null || !user.isEnabled()) {
            log.warn("User account associated with refresh token ({}) is not valid.", usernameFromRefresh);
            throw new UserIsDisabledException();
        }

        return user;
    }

    @Transactional
    public void deleteRefreshTokenByUser(UserEntity user) {
        log.info("deleting refresh token that associated woth user");
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByUser(user)
                .orElseThrow(RefreshTokenNotFoundException::new);
        refreshTokenRepository.delete(refreshToken);
        refreshTokenRepository.flush();
    }

    public void checkRefreshToken(RefreshTokenEntity refreshToken) {
        log.info("checking refresh token {} ", refreshToken);
        log.info("Refresh token id is {}", refreshToken.getId());
        log.info("Refresh token username is {}", refreshToken.getUser().getUsername());
    }
}