package com.backendapp.cms.blogging.user.helper;

import com.backendapp.cms.blogging.contract.AuthenticatedUserContract;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.helper.PrincipalProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrincipalProviderBloggerTest {

    private final PrincipalProvider principalProvider = new PrincipalProvider();
    private final UserEntity user = AuthenticatedUserContract.BLOGGER_USER;
    private final UserEntity userByPrincipal = AuthenticatedUserContract.BLOGGER_USER;

    @Test
    @DisplayName("getPrincipal should return the authenticated UserEntity from SecurityContext")
    void getPrincipal_shouldReturnAuthenticatedUserEntity() {
        // --- ARRANGE ---
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
            when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
            when(mockAuthentication.getPrincipal()).thenReturn(userByPrincipal);

            UserEntity authenticatedUserEntity = principalProvider.getPrincipal();

            assertNotNull(user);
            assertEquals(user, authenticatedUserEntity, "Both must be UserEntity");
            assertEquals(user.getId(), authenticatedUserEntity.getId());
            assertEquals(user.getUsername(), authenticatedUserEntity.getUsername());
            assertEquals(user.getEmail(), authenticatedUserEntity.getEmail());

            mockedStatic.verify(SecurityContextHolder::getContext, times(1));
            verify(mockSecurityContext, times(1)).getAuthentication();
            verify(mockAuthentication, times(1)).getPrincipal();
        }
    }

    @Test
    @DisplayName("getPrincipal should throw ClassCastException if principal is not UserEntity")
    void getPrincipal_shouldThrowClassCastException_whenPrincipalIsNotUserEntity() {
        // ARRANGE
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);

        String nonUserPrincipal = "someOtherPrincipal";

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
            when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
            when(mockAuthentication.getPrincipal()).thenReturn(nonUserPrincipal);

            assertThrows(ClassCastException.class, principalProvider::getPrincipal, "Should throw ClassCastException if principal cannot be cast to UserEntity");

            mockedStatic.verify(SecurityContextHolder::getContext, times(1));
            verify(mockSecurityContext, times(1)).getAuthentication();
            verify(mockAuthentication, times(1)).getPrincipal();
        }
    }

    @Test
    @DisplayName("getPrincipal should throw NullPointerException if Authentication is null")
    void getPrincipal_shouldThrowNullPointerException_whenAuthenticationIsNull() {
        // ARRANGE
        SecurityContext mockSecurityContext = mock(SecurityContext.class);

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
            when(mockSecurityContext.getAuthentication()).thenReturn(null);

            assertThrows(NullPointerException.class, principalProvider::getPrincipal, "Should throw NullPointerException if Authentication is null");

            mockedStatic.verify(SecurityContextHolder::getContext, times(1));
            verify(mockSecurityContext, times(1)).getAuthentication();
        }
    }
}