package com.backendapp.cms.security.jwt;

import com.backendapp.cms.common.constant.ApiConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        log.info("executing shouldNotFilter with URI: {}", requestURI);
        List<String> whiteList = Arrays.asList(
                "/v3/api-docs",
                "/swagger-ui",
                "/swagger-resources",
                "/webjars",
                ApiConstants.AUTH_PATH,
                ApiConstants.PUBLIC_PATH
        );
        return whiteList.stream().anyMatch(requestURI::startsWith) ||
                requestURI.equals("/swagger-ui.html");
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        log.info("executing doFilterInternal with header : {}", authHeader);
        log.info("executing doFilterInternal with request : {}", request);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            log.warn("Header null or header not start with bearer");
            return;
        }

        jwt = authHeader.substring(7);
        log.info("try to get username from authHeader token, validating malformed JWT, and its expired (sekaligus di method extractUsernameFormAccess) ");
        username = jwtService.extractUsernameFromAccess(jwt);

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("set security context");
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if(jwtService.isAccessTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}