package com.backendapp.cms.blogging.helper;

import com.backendapp.cms.blogging.contract.PostRequestContract;
import com.backendapp.cms.blogging.dto.PostRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostSanitizerTest {

    private PostSanitizer postSanitizer;

    @BeforeEach
    void setUp() {
        postSanitizer = new PostSanitizer();
    }

    @Test
    @DisplayName("Should return Converted and Sanitize PostRequestDto")
    void postSanitizer_shouldReturnPostEntityThatWasConvertedAndSanitized() {
        PostRequestDto unconvertedUnsanitizedRequest = PostRequestContract.UNCONVERTED_UNSANITIZED_REQUEST;
        PostRequestDto sanitizedPostRequestDto = PostRequestContract.CONVERTED_SANITIZED_REQUEST;

        PostRequestDto sanitizedPost = postSanitizer.sanitize(unconvertedUnsanitizedRequest);

        assertEquals(sanitizedPost.getTitle(), sanitizedPostRequestDto.getTitle(), "Judul Post yang disimpan harus sama");
        assertEquals(sanitizedPost.getContent(), sanitizedPostRequestDto.getContent(), "Konten HTML Post yang disimpan harus sama");
        assertEquals(sanitizedPost.getExcerpt(), sanitizedPostRequestDto.getExcerpt(), "Excerpt HTML Post yang disimpan harus sama");
        assertEquals(sanitizedPost.getFeaturedImageUrl(), sanitizedPostRequestDto.getFeaturedImageUrl(), "URL Gambar Post yang disimpan harus sama");
        assertEquals(sanitizedPost.getSlug(), sanitizedPostRequestDto.getSlug(), "Slug Post yang disimpan harus sama");
        assertEquals(sanitizedPost.getStatus(), sanitizedPostRequestDto.getStatus(), "Status Post yang disimpan harus sama");
        assertEquals(sanitizedPost.getCategories(), sanitizedPostRequestDto.getCategories(), "Kategori Post yang disimpan harus sama");
    }

    @Test
    @DisplayName("Should return converted but unsanitized PostRequestDto")
    void setPostSanitizer_shouldReturnPostEntityThatWasConvertedAndSanitized() {
        PostRequestDto unconvertedUnsanitizedRequest = PostRequestContract.UNCONVERTED_UNSANITIZED_REQUEST;
        PostRequestDto convertedButUnsanitized = PostRequestContract.CONVERTED_UNSANITIZED_REQUEST;

        PostRequestDto sanitizedPost = postSanitizer.convertToHtml(unconvertedUnsanitizedRequest);

        assertEquals(sanitizedPost, convertedButUnsanitized, "Hasil dari sanitizer harus sama");
        assertEquals(sanitizedPost.getTitle(), convertedButUnsanitized.getTitle(), "Judul Post yang disimpan harus sama");
        assertEquals(sanitizedPost.getContent(), convertedButUnsanitized.getContent(), "Konten HTML Post yang disimpan harus sama");
        assertEquals(sanitizedPost.getExcerpt(), convertedButUnsanitized.getExcerpt(), "Excerpt HTML Post yang disimpan harus sama");
        assertEquals(sanitizedPost.getFeaturedImageUrl(), convertedButUnsanitized.getFeaturedImageUrl(), "URL Gambar Post yang disimpan harus sama");
        assertEquals(sanitizedPost.getSlug(), convertedButUnsanitized.getSlug(), "Slug Post yang disimpan harus sama");
        assertEquals(sanitizedPost.getStatus(), convertedButUnsanitized.getStatus(), "Status Post yang disimpan harus sama");
        assertEquals(sanitizedPost.getCategories(), convertedButUnsanitized.getCategories(), "Kategori Post yang disimpan harus sama");
    }
}