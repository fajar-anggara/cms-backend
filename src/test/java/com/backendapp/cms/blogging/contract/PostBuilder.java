package com.backendapp.cms.blogging.contract;

import com.backendapp.cms.blogging.dto.PostRequestDto;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.common.enums.Status;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.openapi.dto.PostRequest;
import com.backendapp.cms.openapi.dto.PostSimpleResponse;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


public class PostBuilder {

    private Long id;
    private String title;
    private String slug;
    private String content;
    private String excerpt;
    private String featuredImageUrl;
    private Status status;
    private UserEntity user;
    private Set<CategoryEntity> categories = new HashSet<>();
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public PostBuilder() {
        this.id = null;
        this.title = null;
        this.slug = null;
        this.content = null;
        this.excerpt = null;
        this.featuredImageUrl = null;
        this.status = null;
        this.user = null;
        this.categories = new HashSet<>();
        this.publishedAt = null;
        this.createdAt = null;
        this.updatedAt = null;
        this.deletedAt = null;
    }

    public PostBuilder withStatus(Status status) {
        this.status = status;
        return this;
    }

    public PostBuilder withUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public PostBuilder withCategories(Set<CategoryEntity> categories) {
        this.categories = categories;
        return this;
    }

    public PostBuilder withPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }

    public PostBuilder withCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public PostBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public PostBuilder withDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public PostBuilder withDefault() {
        this.id = 1L;
        this.title = "My First Blog Post";
        this.slug = "my-first-blog-post";
        this.content = "This is the **amazing** content of my first blog post.";
        this.excerpt = "A short summary of the post.";
        this.featuredImageUrl = "https://example.com/images/default-featured.jpg";
        this.status = Status.PUBLISHED;
        this.user = new UserBuilder().withDefault().build();
        this.categories = new CategoryBuilder().withDefault().buildSet(1);
        this.publishedAt = LocalDateTime.of(2024, 7, 12, 10, 0, 0);
        this.createdAt = LocalDateTime.of(2024, 7, 11, 15, 30, 0);
        this.updatedAt = LocalDateTime.of(2024, 7, 12, 10, 0, 0);
        this.deletedAt = null;
        return this;
    }

    public PostBuilder withXss() {
        this.title = "<script>alert('XSS Title!');</script>My Title";
        this.content = "<img src=x onerror=alert('XSS Content');>Hello <b>World</b>!";
        this.excerpt = "<link rel='stylesheet' href='evil.css' onload='alert(\"XSS Excerpt\");'>";
        this.featuredImageUrl = "javascript:alert('XSS URL');"; // Contoh XSS di URL
        this.slug = "xss-post-slug"; // Slug juga bisa disanitasi
        return this;
    }

    public PostBuilder withMarkDown() {
        this.title = "Post with Basic Markdown Content";
        this.slug = "post-with-basic-markdown";
        this.content = "# Heading 1\n\nThis is a paragraph with **bold** and *italic* text.\n\n" +
                "- List item 1\n" +
                "- List item 2\n" +
                "  - Nested list item\n\n" +
                "> This is a blockquote.\n\n" +
                "Strikethrough text and a [link](https://example.com).";
        this.excerpt = "A short summary with *basic* Markdown and `code`.";
        return this;
    }

    public PostBuilder withUnsupportedMarkDown() {
        this.title = "Post with Unsupported Markdown";
        this.slug = "post-with-unsupported-markdown";
        this.content = "This is a paragraph.\n\n" +
                "[[Internal Link]]\n\n" + // Not standard Markdown
                "{::comment}This is a comment{:/comment}\n\n" + // Not standard Markdown
                "==Highlight Text==\n\n" + // Not standard Markdown
                "[TOC]\n\n" + // Not standard Markdown
                "This is *standard* Markdown after unsupported syntax.";
        this.excerpt = "Summary with ==highlight== and [[link]].";
        return this;
    }

    public PostBuilder withEmptyOrNullFields() {
        this.title = "";
        this.slug = "";
        this.content = "";
        this.excerpt = null;
        this.featuredImageUrl = "";
        this.status = null;
        this.user = null;
        this.categories = new HashSet<>();
        this.publishedAt = null;
        this.createdAt = null;
        this.updatedAt = null;
        this.deletedAt = null;
        return this;
    }

    public PostBuilder withStatusDraft() {
        this.status = Status.DRAFT;
        this.publishedAt = null; // Draft post should not have published date
        return this;
    }

    public PostBuilder withStatusDeleted() {
        this.deletedAt = LocalDateTime.now();
        return this;
    }

    public PostEntity build() {
        return PostEntity.builder()
                .id(this.id)
                .title(this.title)
                .slug(this.slug)
                .content(this.content)
                .excerpt(this.excerpt)
                .featuredImageUrl(this.featuredImageUrl)
                .status(this.status)
                .user(this.user)
                .categories(this.categories)
                .publishedAt(this.publishedAt)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .deletedAt(this.deletedAt)
                .build();
    }

    public PostRequestDto buildXssExpectedResult() {
        String expectedSanitizedTitle = "My Title";
        String expectedSanitizedSlug = "xss-post-slug";
        String expectedSanitizedContent = "<p>&lt;img src&#61;x onerror&#61;alert(&#39;XSS Content&#39;);&gt;Hello <b>World</b>!</p>";
        String expectedSanitizedExcerpt = "";

        return PostRequestDto.builder()
                .title(expectedSanitizedTitle)
                .slug(Optional.of(expectedSanitizedSlug))
                .content(expectedSanitizedContent)
                .excerpt(Optional.of(expectedSanitizedExcerpt))
                .featuredImageUrl(Optional.of("/default.jpg"))
                .status(Optional.of(Status.valueOf(this.status.toString())))
                .categories(Optional.of(new CategoryBuilder().withDefault().buildListCategoriesSimpleDTO(1)))
                .build();
    }

    public PostRequestDto buildConvertedToHtml() {
        String expectedTitle = "<p>Post with Basic Markdown Content</p>";
        String expectedHtmlContent = "<h1>Heading 1</h1>\n" +
                "<p>This is a paragraph with <strong>bold</strong> and <em>italic</em> text.</p>\n" +
                "<ul>\n" +
                "<li>List item 1</li>\n" +
                "<li>List item 2\n" + //
                "<ul>\n" +
                "<li>Nested list item</li>\n" +
                "</ul>\n" +
                "</li>\n" +
                "</ul>\n" +
                "<blockquote>\n" +
                "<p>This is a blockquote.</p>\n" +
                "</blockquote>\n" +
                "<p>Strikethrough text and a <a href=\"https://example.com\">link</a>.</p>\n";

        String expectedHtmlExcerpt = "<p>A short summary with <em>basic</em> Markdown and <code>code</code>.</p>\n";

        return PostRequestDto.builder()
                .title(expectedTitle)
                .slug(Optional.of("post-with-basic-markdown"))
                .content(expectedHtmlContent)
                .excerpt(Optional.of(expectedHtmlExcerpt))
                .featuredImageUrl(Optional.of(this.featuredImageUrl))
                .status(Optional.of(this.status))
                .categories(Optional.of(new CategoryBuilder().withDefault().buildListCategoriesSimpleDTO(1)))
                .build();
    }

    public PostRequestDto buildSanitizedHtml() {
        String expectedTitle = "Post with Basic Markdown Content";
        String expectedHtmlContent = "<h1>Heading 1</h1>\n" +
                "<p>This is a paragraph with <strong>bold</strong> and <em>italic</em> text.</p>\n" +
                "<ul>\n" +
                "<li>List item 1</li>\n" +
                "<li>List item 2\n" + //
                "<ul>\n" +
                "<li>Nested list item</li>\n" +
                "</ul>\n" +
                "</li>\n" +
                "</ul>\n" +
                "<blockquote>\n" +
                "<p>This is a blockquote.</p>\n" +
                "</blockquote>\n" +
                "<p>Strikethrough text and a <a href=\"https://example.com\">link</a>.</p>\n";

        String expectedHtmlExcerpt = "A short summary with basic Markdown and code.";

        return PostRequestDto.builder()
                .title(expectedTitle)
                .slug(Optional.of("post-with-basic-markdown"))
                .content(expectedHtmlContent)
                .excerpt(Optional.of(expectedHtmlExcerpt))
                .featuredImageUrl(Optional.of(this.featuredImageUrl))
                .status(Optional.of(this.status))
                .categories(Optional.of(new CategoryBuilder().withDefault().buildListCategoriesSimpleDTO(1)))
                .build();
    }

    public PostRequestDto buildConvertedToHtmlUnsupportedMarkdown() {
        String expectedTitle = "<p>Post with Unsupported Markdown</p>";
        String expectedHtmlContent = "<p>This is a paragraph.</p>\n" +
                "<p>[[Internal Link]]</p>\n" +
                "<p>{::comment}This is a comment{:/comment}</p>\n" +
                "<p>==Highlight Text==</p>\n" +
                "<p>[TOC]</p>\n" +
                "<p>This is <em>standard</em> Markdown after unsupported syntax.</p>\n";

        String expectedHtmlExcerpt = "<p>Summary with ==highlight== and [[link]].</p>\n";

        return PostRequestDto.builder()
                .title(expectedTitle)
                .slug(Optional.ofNullable(this.slug))
                .content(expectedHtmlContent)
                .excerpt(Optional.of(expectedHtmlExcerpt))
                .featuredImageUrl(Optional.ofNullable(this.featuredImageUrl))
                .status(Optional.ofNullable(this.status))
                .categories(Optional.of(new CategoryBuilder().withDefault().buildListCategoriesSimpleDTO(1)))
                .build();
    }

    public PostRequestDto buildPostRequestDto() {
        return PostRequestDto
                .builder()
                .title(this.title)
                .slug(Optional.of(this.slug))
                .content(this.content)
                .excerpt(Optional.of(this.excerpt))
                .featuredImageUrl(Optional.of(this.featuredImageUrl))
                .status(Optional.of(this.status))
                .categories(Optional.of(new CategoryBuilder().withDefault().buildListCategoriesSimpleDTO(1)))
                .build();
    }


    /**
     * @param count = menentukan akan menghasilkan berapa List.
     * @return Jika count = 1 maka akan mengembalikan list of withDefault.
     */
    public List<PostEntity> buildList(int count) {
        Long currId = this.id;
        List<PostEntity> posts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            PostEntity entity = PostEntity.builder()
                    .id(currId)
                    .title(this.title != null ? this.title : null)
                    .slug(this.slug != null ? this.slug + (i + 1) : null)
                    .content(this.content)
                    .excerpt(this.excerpt)
                    .featuredImageUrl(this.featuredImageUrl)
                    .status(this.status)
                    .user(this.user)
                    .categories(this.categories)
                    .publishedAt(this.publishedAt)
                    .createdAt(this.createdAt)
                    .updatedAt(this.updatedAt)
                    .deletedAt(this.deletedAt)
                    .build();
            currId++;

            posts.add(entity);
        }
        return posts;
    }

    public PostSimpleResponse buildPostSimpleResponse() {
        PostSimpleResponse postSimpleResponse = new PostSimpleResponse();
        postSimpleResponse.setId(this.id);
        postSimpleResponse.setTitle(this.title);
        postSimpleResponse.setSlug(this.slug);
        postSimpleResponse.setExcerpt(this.excerpt);
        postSimpleResponse.setFeaturedImageUrl(this.featuredImageUrl);
        postSimpleResponse.setStatus(PostSimpleResponse.StatusEnum.fromValue(this.status.toString()));
        postSimpleResponse.setUser(new UserBuilder().withDefault().buildUserSimpleResponse());
        postSimpleResponse.setCategories(new CategoryBuilder().withDefault().buildListCategoriesSimpleDTO(1));
        postSimpleResponse.setPublishedAt(this.publishedAt.atZone(ZoneId.systemDefault()).toOffsetDateTime());
        postSimpleResponse.setCreatedAt(this.createdAt.atZone(ZoneId.systemDefault()).toOffsetDateTime());

        return postSimpleResponse;
    }

    public PostRequest buildPostRequest() {
        PostRequest postRequest = new PostRequest();
        postRequest.setTitle(this.title);
        postRequest.setSlug(this.slug);
        postRequest.setContent(this.content);
        postRequest.setExcerpt(this.excerpt);
        postRequest.setFeaturedImageUrl(this.featuredImageUrl);
        postRequest.setStatus(PostRequest.StatusEnum.valueOf(this.status.toString()));
        postRequest.setCategories(new CategoryBuilder().withDefault().buildListCategoriesSimpleDTO(1));

        return postRequest;
    }

}