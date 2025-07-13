package com.backendapp.cms.blogging.helper;

import com.backendapp.cms.blogging.contract.PostBuilder;
import com.backendapp.cms.blogging.dto.PostRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostSanitizerTest {

    private final PostSanitizer postSanitizer = new PostSanitizer();

    @Test
    @DisplayName("Should return HTML by converting from PostRequestDto")
    void convertToHtml_shouldReturnHtmlByConvertingFromPostRequestDto() {
        PostRequestDto unconvertedRequest = new PostBuilder()
                .withDefault()
                .withMarkDown()
                .buildPostRequestDto();

        PostRequestDto convertedRequest = new PostBuilder()
                .withDefault()
                .withMarkDown()
                .buildConvertedToHtml();

        PostRequestDto actual = postSanitizer.convertToHtml(unconvertedRequest);

        assertEquals(
                actual.toString()
                        .trim()
                        .replace("\r", "").replace("\n", ""),
                convertedRequest.toString()
                        .trim()
                        .replace("\r", "").replace("\n", ""));
    }

    @Test
    @DisplayName("Should ignore unsupported MarkDown sintaks")
    void convertToHtml_shouldReturnUnsupportedMarkdownSintaks() {
        PostRequestDto unconvertedRequest = new PostBuilder()
                .withDefault()
                .withUnsupportedMarkDown()
                .buildPostRequestDto();

        PostRequestDto convertedRequest = new PostBuilder()
                .withDefault()
                .withUnsupportedMarkDown()
                .buildConvertedToHtmlUnsupportedMarkdown();

        PostRequestDto actual = postSanitizer.convertToHtml(unconvertedRequest);

        assertEquals(
                actual.toString()
                        .trim()
                        .replace("\r", "").replace("\n", ""),
                convertedRequest.toString()
                        .trim()
                        .replace("\r", "").replace("\n", ""));
    }

    @Test
    @DisplayName("Should remove all xss sintaks from html")
    void sanitize_shouldRemoveAllXssSintaksFromHtml() {
        PostRequestDto xssRequest = new PostBuilder()
                .withDefault()
                .withXss()
                .buildPostRequestDto();

        PostRequestDto sanitizedXssRequest = new PostBuilder()
                .withDefault()
                .withXss()
                .buildXssExpectedResult();

        PostRequestDto actual = postSanitizer.sanitize(xssRequest);

        assertEquals(
                actual.toString()
                        .trim()
                        .replace("\r", "").replace("\n", ""),
                sanitizedXssRequest.toString()
                        .trim()
                        .replace("\r", "").replace("\n", ""));
    }

    @Test
    @DisplayName("Should return sanitized normal Html when given normal html")
    void sanitize_shouldReturnNormalHtmlWhenGivenNormalHtml() {
        PostRequestDto markdownRequest = new PostBuilder()
                .withDefault()
                .withMarkDown()
                .buildPostRequestDto();

        PostRequestDto sanitizedMarkdownRequest = new PostBuilder()
                .withDefault()
                .withMarkDown()
                .buildSanitizedHtml();

        PostRequestDto actual = postSanitizer.sanitize(markdownRequest);

        assertEquals(
                actual.toString()
                        .trim()
                        .replace("\r", "").replace("\n", ""),
                sanitizedMarkdownRequest.toString()
                        .trim()
                        .replace("\r", "").replace("\n", ""));
    }
}