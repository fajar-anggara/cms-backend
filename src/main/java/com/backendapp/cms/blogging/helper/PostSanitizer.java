package com.backendapp.cms.blogging.helper;

import com.backendapp.cms.blogging.dto.PostRequestDto;
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
    private final PolicyFactory titlePolicy = new HtmlPolicyBuilder()
            .allowElements("em", "strong", "i", "b") // Hanya basic formatting
            .toFactory();

    private final PolicyFactory excerptPolicy = new HtmlPolicyBuilder()
            .allowElements("p", "em", "strong", "i", "b", "br")
            .toFactory();

    private final PolicyFactory contentPolicy = new HtmlPolicyBuilder()
            .allowElements("p", "br", "div")
            .allowElements("strong", "em", "b", "i", "u", "s", "small", "mark")
            .allowElements("sub", "sup", "abbr", "cite", "code", "pre")
            .allowElements("h1", "h2", "h3", "h4", "h5", "h6")
            .allowElements("ul", "ol", "li", "dl", "dt", "dd")
            .allowElements("blockquote", "q")
            .allowElements("a")
            .allowAttributes("href").matching(
                    Pattern.compile("^(?:https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=]*|/[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=]*|[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=]*)$")
            ).onElements("a")
            .allowAttributes("title").onElements("a")
            .allowElements("img")
            .allowAttributes("src").matching(
                    Pattern.compile("^https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=]*\\.(jpg|jpeg|png|gif|webp|svg)(?:\\?[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=]*)?$", Pattern.CASE_INSENSITIVE)
            ).onElements("img")
            .allowAttributes("alt", "title").onElements("img")
            .allowAttributes("width", "height").matching(Pattern.compile("^[0-9]+$")).onElements("img")
            .allowElements("table", "thead", "tbody", "tfoot", "tr", "td", "th")
            .allowAttributes("colspan", "rowspan").matching(Pattern.compile("^[0-9]+$")).onElements("td", "th")
            .allowElements("hr", "span")
            .allowAttributes("class").matching(Pattern.compile("^[\\w\\-\\s]+$")).globally()
            .allowAttributes("id").matching(Pattern.compile("^[\\w\\-]+$")).globally()
            .toFactory();

    public PostRequestDto sanitize(PostRequestDto unSanitizedPostRequest) {
        PostRequestDto convertedPostRequest = convertToHtml(unSanitizedPostRequest);

        String sanitizedTitle = Optional.of(convertedPostRequest.getTitle())
                .map(titlePolicy::sanitize)
                .map(String::trim)
                .orElse(unSanitizedPostRequest.getTitle());

        String sanitizedContent = Optional.of(convertedPostRequest.getContent())
                .map(contentPolicy::sanitize)
                .orElse(unSanitizedPostRequest.getContent());

        Optional<String> sanitizedExcerpt = convertedPostRequest.getExcerpt()
                .map(excerptPolicy::sanitize);

        Optional<String> sanitizedImageUrl = convertedPostRequest.getFeaturedImageUrl()
                .map(plainText::sanitize);

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
}
