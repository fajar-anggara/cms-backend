package com.backendapp.cms.blogging.converter;


import com.backendapp.cms.blogging.dto.PostRequestDto;
import com.backendapp.cms.openapi.dto.PostRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PostRequestConverterTest {

    @Autowired
    PostRequestConverter postRequestConverter;

    @Test
    @DisplayName("Should map from postRequest to PostRequestDto")
    void PostRequestConverter_shouldMapFromPostRequestToPostRequestDto() {
        PostRequest rawPostRequest = PostSanitizerContract.UNCONVERTED_UNSANITIZED_RAWREQUEST;
        PostRequestDto postRequestDto = PostSanitizerContract.UNCONVERTED_UNSANITIZED_REQUEST; PostRequestDto mappedPostRequestDto = postRequestConverter.fromPostRequestToPostRequestDto(rawPostRequest);

        assertEquals(mappedPostRequestDto, postRequestDto, "Harus ter mapped dengan baik");
    }
}
