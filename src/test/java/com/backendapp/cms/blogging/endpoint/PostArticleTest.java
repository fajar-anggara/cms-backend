package com.backendapp.cms.blogging.endpoint;

import com.backendapp.cms.blogging.contract.AuthenticatedUserContract;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.helper.PrincipalProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;


@WebMvcTest(BloggingEndpoint.class)
public class PostArticleTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    PrincipalProvider principalProvider;

    @MockitoBean
    PostArticleOperationPerformer postArticleOperationPerformer;
    List
    @InjectMocks
    BloggingEndpoint bloggingEndpoint;

    @Test
    @DisplayName("ajkdfaj")
    void postArticle_ShouldReturnUserEntityWhenCall() throws Exception {
        UserEntity mockUser = AuthenticatedUserContract.BLOGGER_USER;
        when(principalProvider.getPrincipal()).thenReturn(mockUser);

        UserEntity user = principalProvider.getPrincipal();
    }
}


