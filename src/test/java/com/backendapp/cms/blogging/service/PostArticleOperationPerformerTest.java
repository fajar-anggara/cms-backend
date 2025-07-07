package com.backendapp.cms.blogging.service;

import com.backendapp.cms.blogging.contract.PostRequestContract;
import com.backendapp.cms.blogging.dto.PostRequestDto;
import com.backendapp.cms.blogging.helper.PostGenerator;
import com.backendapp.cms.blogging.helper.PostSanitizer;
import com.backendapp.cms.users.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ExtendWith(MockitoExtension.class)
public class PostArticleOperationPerformerTest {

    PostSanitizer postSanitizer = new PostSanitizer();

    @MockitoBean
    UserEntity userEntity;

    @Test
    @DisplayName("Happy path - This should excecute the perfect success path")
    void PostArticleOperationPerformer_shouldReturnPostEntityThatWasSaved() {
        PostRequestDto request = PostRequestContract.UNCONVERTED_UNSANITIZED_REQUEST;
        PostRequestDto sanitizedPost = postSanitizer.sanitize(request);



    }

}
