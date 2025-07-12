package com.backendapp.cms.blogging.converter;

import com.backendapp.cms.blogging.contract.PostBuilder;
import com.backendapp.cms.blogging.contract.bonded.AuthenticatedUserDummy;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.openapi.dto.PostSimpleResponse;
import com.backendapp.cms.openapi.dto.UserSimpleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PostResponseConverterTest {

    private final PostBuilder postBuilder = new PostBuilder();
    private PostResponseConverter postResponseConverter = new PostResponseConverterImpl();

    @Test
    @DisplayName("Should return PostSimpleResponse with correct value")
    void fromPostEntityToPostSimpleResponse_shouldReturnCorrectPostSimpleResponse() {
        PostEntity post = postBuilder.withDefault().build();
        PostSimpleResponse postToSendToResponse = postBuilder.withDefault().buildPostSimpleResponse();



    }

}
