package com.backendapp.cms.blogging.converter;

import com.backendapp.cms.blogging.contract.PostBuilder;
import com.backendapp.cms.blogging.converter.mapper.PostResponseMapper;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.openapi.dto.PostSimpleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PostResponseConverterTest {

    private final PostBuilder postBuilder = new PostBuilder();

    @Autowired
    private PostResponseConverter postResponseConverter;

    @Test
    @DisplayName("Should return PostSimpleResponse with correct value")
    void fromPostEntityToPostSimpleResponse_shouldReturnCorrectPostSimpleResponse() {
        PostEntity post = postBuilder.withDefault().build();
        PostSimpleResponse postToSendToResponse = postBuilder.withDefault().buildPostSimpleResponse();

        PostSimpleResponse actual = postResponseConverter.fromPostEntityToPostSimpleResponse(post);

        assertEquals(actual, postToSendToResponse);
    }

}
