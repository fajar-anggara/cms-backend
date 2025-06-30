package com.backendapp.cms.security.jwt;

import com.backendapp.cms.common.constant.AppConstants;
import com.backendapp.cms.security.dto.AuthenticationResponse;
import com.backendapp.cms.security.entity.RefreshTokenEntity;
import com.backendapp.cms.security.repository.RefreshTokenRepository;
import com.backendapp.cms.users.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
@AllArgsConstructor
public class JwtService {

    private final AppConstants appConstants;
    private final RefreshTokenRepository refreshTokenRepository;

    public String extractUsernameFromAccess(String token) {
        log.info("Attempting to extract subject from access token, will call method extractUsernameFromAccess");
        return extractClaimFromAccess(token, Claims::getSubject);
    }

    public String extractUsernameFromAccessIgnoreExpired(String token) {
        log.info("Attempting to extract subject from access token ignoring the expired token, will call method extractClaimFromAccessIgnoreExpired");
        return extractClaimFromAccessIgnoreExpired(token, Claims::getSubject);
    }

    public String extractUsernameFromRefresh(String token) {
        log.info("Attempting to extract subject from refresh token, will call method extractClaimFromRefresh");
        return extractClaimFromRefresh(token, Claims::getSubject);
    }

    public <T> T extractClaimFromAccess(String token, Function<Claims, T> claimsResolver) {
        log.info("used By extractUsernameFromAccess and extractExpirationFromAccess, will call method extractClaimFromAccess");
        final Claims claims = extractAccessTokenClaims(token);
        return claimsResolver.apply(claims);
    }

    public <T> T extractClaimFromAccessIgnoreExpired(String token, Function<Claims, T> claimsResolver) {
        log.info("used By extractUsernameFromAccessIgnoreExpired, will call method extractAccessTokenClaimsIgnoreExpired");
        final Claims claims = extractAccessTokenClaimsIgnoreExpired(token);
        return claimsResolver.apply(claims);
    }

    public <T> T extractClaimFromRefresh(String token, Function<Claims, T> claimsResolver) {
        log.info("used By extractUsernameFromRefresh, extractExpirationFromRefresh, and validateRefreshAndAccessToken will call extractRefreshTokenClaims");
        final Claims claims = extractRefreshTokenClaims(token);
        return claimsResolver.apply(claims);
    }

    public AuthenticationResponse generateTokenAndRefreshToken(UserDetails userDetails) {
        log.info("generating access and refresh Token");
        String accessToken = generateAccessToken(userDetails);
        String refreshToken = generateRefreshToken(userDetails);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public String getAuthorities(Collection<? extends GrantedAuthority> userDetailsAuthority) {
        if (userDetailsAuthority != null && !userDetailsAuthority.isEmpty()) {
            return userDetailsAuthority.iterator().next().getAuthority();
        } else {
            return "ROLE_ANONYMOUS";
        }
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof UserEntity user) {
            claims.put("authority", getAuthorities(user.getAuthorities()));
            // claims.put("userId", user.getUsername());
        }

        return Jwts
                .builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(appConstants.JWT_ACCESS_TOKEN_EXPIRED)))
                .signWith((SecretKey) appConstants.JWT_ACCESS_TOKEN_SECRET_KEY)
                .compact();
    }

    @Transactional
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        /*
         * if (userDetails instanceof UserEntity user) {
         * claims.put("userId", user.getId());
         * }
         */

        Date issuedAt = Date.from(Instant.now());
        Date expiration = Date.from(Instant.now().plusMillis(appConstants.JWT_REFRESH_TOKEN_EXPIRED));
        String id = UUID.randomUUID().toString();

        log.info("Savind refresh token with id {} and expiration {}", id, expiration);
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setId(id);
        refreshTokenEntity.setUser((UserEntity) userDetails);
        refreshTokenEntity.setExpiredTime(expiration);
        RefreshTokenEntity refreshToken = refreshTokenRepository.save(refreshTokenEntity);

        log.info("Building refresh token");
        return Jwts.builder()
                .claims(claims)
                .subject(refreshToken.getUser().getUsername())
                .issuedAt(issuedAt)
                .expiration(refreshToken.getExpiredTime())
                .id(refreshToken.getId())
                .signWith(appConstants.JWT_REFRESH_TOKEN_SECRET_KEY)
                .compact();
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsernameFromAccess(token);
        return (username.equals(userDetails.getUsername())) && !isAccessTokenExpired(token);
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsernameFromRefresh(token);
        return (username.equals(userDetails.getUsername())) && !isRefreshTokenExpired(token);
    }

    private boolean isAccessTokenExpired(String token) {
        return extractExpirationFromAccess(token).before(Date.from(Instant.now()));
    }

    public boolean isRefreshTokenExpired(String token) {
        return extractExpirationFromRefresh(token).before(Date.from(Instant.now()));
    }

    private Date extractExpirationFromAccess(String token) {
        return extractClaimFromAccess(token, Claims::getExpiration);
    }

    private Date extractExpirationFromRefresh(String token) {
        return extractClaimFromRefresh(token, Claims::getExpiration);
    }

    private Claims extractAccessTokenClaims(String token) {
        Jws<Claims> jwsClaims = Jwts.parser()
                .verifyWith((SecretKey) appConstants.JWT_ACCESS_TOKEN_SECRET_KEY)
                .build()
                .parseSignedClaims(token);

        return jwsClaims.getPayload();
    }

    private Claims extractAccessTokenClaimsIgnoreExpired(String token) {
        Jws<Claims> jwsClaims = Jwts.parser()
                .verifyWith(appConstants.JWT_ACCESS_TOKEN_SECRET_KEY)
                .clockSkewSeconds(3600 * 24 * 365 * 10)
                .build()
                .parseSignedClaims(token);

        return jwsClaims.getPayload();
    }

    private Claims extractRefreshTokenClaims(String refreshToken) {
        Jws<Claims> jwsClaims = Jwts.parser()
                .verifyWith(appConstants.JWT_REFRESH_TOKEN_SECRET_KEY)
                .build()
                .parseSignedClaims(refreshToken);

        return jwsClaims.getPayload();
    }
}