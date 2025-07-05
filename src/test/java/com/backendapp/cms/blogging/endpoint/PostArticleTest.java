package com.backendapp.cms.blogging.endpoint;

import com.backendapp.cms.users.helper.PrincipalProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(BloggingEndpoint.class)
public class PostArticleTest {

    @Mock
    PrincipalProvider principalProvider;

    @Test
    void postArticle_ShouldReturn200OkWhenPostAnArticle() throws Exception {

    }
}
