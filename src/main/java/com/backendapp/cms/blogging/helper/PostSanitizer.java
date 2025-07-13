package com.backendapp.cms.blogging.helper;

import com.backendapp.cms.blogging.dto.PostRequestDto;
import com.backendapp.cms.common.constant.BloggingConstant;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class PostSanitizer {

    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();
    private final PolicyFactory plainText = new HtmlPolicyBuilder().toFactory();
    private static final Pattern safeImageUrl = Pattern.compile(
            "^https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=]+\\.(jpg|jpeg|png|gif|webp|svg)$",
    Pattern.CASE_INSENSITIVE
    );

    private final PolicyFactory contentPolicy = new HtmlPolicyBuilder()
            // Allowed tags and attributes explicitly
            .allowElements("p", "b", "i", "ul", "ol", "li", "code", "pre", "strong", "em", "h1", "h2", "h3")
            .allowUrlProtocols("https", "mailto")
            .allowElements("img")
            .allowElements("blockquote")
            .allowAttributes("src").matching(Pattern.compile("^https?://.*\\.(jpg|jpeg|png|gif|webp|svg)$", Pattern.CASE_INSENSITIVE)).onElements("img")
            .allowAttributes("alt", "title", "width", "height").onElements("img")
            .allowElements("a")
            .allowAttributes("href").matching(Pattern.compile("^(https?://|mailto:)[^\\s]+$")).onElements("a")
            .allowAttributes("title").onElements("a")
            .toFactory();

    public PostRequestDto sanitize(PostRequestDto unSanitizedPostRequest) {
        PostRequestDto convertedPostRequest = convertToHtml(unSanitizedPostRequest);

        String sanitizedTitle = Optional.of(convertedPostRequest.getTitle())
                .map(plainText::sanitize)
                .map(String::trim)
                .orElse(unSanitizedPostRequest.getTitle());

        String sanitizedContent = Optional.of(convertedPostRequest.getContent())
                .map(contentPolicy::sanitize)
                .orElse(unSanitizedPostRequest.getContent());

        Optional<String> sanitizedExcerpt = convertedPostRequest.getExcerpt()
                .map(plainText::sanitize);

        Optional<String> sanitizedImageUrl = convertedPostRequest.getFeaturedImageUrl()
                .map(this::isImageSave);

        return PostRequestDto.builder()
                .title(sanitizedTitle)
                .slug(unSanitizedPostRequest.getSlug())
                .content(sanitizedContent)
                .excerpt(sanitizedExcerpt)
                .featuredImageUrl(sanitizedImageUrl)
                .status(unSanitizedPostRequest.getStatus())
                .categories(unSanitizedPostRequest.getCategories())
                .build();
    }

    public PostRequestDto convertToHtml(PostRequestDto unConvertedPostRequest) {

        String convertedTitle = Optional.ofNullable(unConvertedPostRequest.getTitle())
                .map(parser::parse)
                .map(renderer::render)
                .orElse(null);

        String convertedContent = Optional.ofNullable(unConvertedPostRequest.getContent())
                .map(parser::parse)
                .map(renderer::render)
                .orElse(null);

        String convertedExcerpt = unConvertedPostRequest.getExcerpt()
                .map(parser::parse)
                .map(renderer::render)
                .orElse(null);

        return PostRequestDto.builder()
                .title(convertedTitle)
                .slug(unConvertedPostRequest.getSlug())
                .content(convertedContent)
                .excerpt(Optional.ofNullable(convertedExcerpt))
                .featuredImageUrl(unConvertedPostRequest.getFeaturedImageUrl())
                .status(unConvertedPostRequest.getStatus())
                .categories(unConvertedPostRequest.getCategories())
                .build();
    }

    public String isImageSave(String imageUrl) {
        if (safeImageUrl.matcher(imageUrl).matches()) {
            return imageUrl;
        }
        return BloggingConstant.POST_IMAGE_DEFAULT;
    }
}
