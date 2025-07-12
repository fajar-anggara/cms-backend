package com.backendapp.cms.blogging.converter;


import com.backendapp.cms.blogging.contract.PostBuilder;
import com.backendapp.cms.blogging.dto.PostRequestDto;
import com.backendapp.cms.openapi.dto.PostRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PostRequestConverterTest {

    private final PostBuilder postBuilder = new PostBuilder();

    @Autowired
    PostRequestConverter postRequestConverter;

    @Test
    @DisplayName("Should return PostRequestDto from raw PostRequest")
    void fromPostRequestToPostRequestDto_shouldReturnPostRequestDto() {
        PostRequest postRequest = postBuilder.withDefault().buildPostRequest();
        PostRequestDto postRequestDto = postBuilder.withDefault().buildPostRequestDto();

        PostRequestDto actual = postRequestConverter.fromPostRequestToPostRequestDto(postRequest);

        assertEquals(actual, postRequestDto);
    }

}
