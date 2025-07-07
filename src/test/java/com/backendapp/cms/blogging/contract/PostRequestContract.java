package com.backendapp.cms.blogging.contract;

import com.backendapp.cms.blogging.dto.PostRequestDto;
import com.backendapp.cms.common.enums.Status;
import com.backendapp.cms.openapi.dto.PostRequest;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PostRequestContract {

    public static final PostRequest UNCONVERTED_UNSANITIZED_RAWREQUEST;
    public static final PostRequestDto UNCONVERTED_UNSANITIZED_REQUEST;
    public static final PostRequestDto CONVERTED_UNSANITIZED_REQUEST;
    public static final PostRequestDto CONVERTED_SANITIZED_REQUEST;

    static {
        String rawRequestTitle = "Judul <script>alert('XSS di title!');</script> *Test*";
        String rawRequestContent =
                "Ini adalah **konten utama**.<br>" +
                        "Dengan link berbahaya: <a href=\"javascript:alert('XSS Link');\">Klik</a>.<br>" +
                        "Gambar injeksi: <img src=\"x\" onerror=\"alert('XSS Gambar');\"><br>" +
                        "Dan tag tidak diizinkan: <iframe src=\"http://malicious.com\"></iframe> <marquee>Scroll</marquee><br>" +
                        "Ini Markdown:\n* Item 1\n* Item 2";
        String rawRequestExcerpt = "Excerpt pendek <img src=\"non-existent\" onload=\"alert('XSS');\"> *test*.";
        String rawRequestImageUrl = "http://evil.com/image.jpg?param=<script>alert('url-xss')</script>";

        UNCONVERTED_UNSANITIZED_RAWREQUEST = new PostRequest();
        UNCONVERTED_UNSANITIZED_RAWREQUEST.setTitle(rawRequestTitle);
        UNCONVERTED_UNSANITIZED_RAWREQUEST.setSlug("judul-test");
        UNCONVERTED_UNSANITIZED_RAWREQUEST.setContent(rawRequestContent);
        UNCONVERTED_UNSANITIZED_RAWREQUEST.setExcerpt(rawRequestExcerpt);
        UNCONVERTED_UNSANITIZED_RAWREQUEST.setFeaturedImageUrl(rawRequestImageUrl);
        UNCONVERTED_UNSANITIZED_RAWREQUEST.setStatus(PostRequest.StatusEnum.DRAFT);
        UNCONVERTED_UNSANITIZED_RAWREQUEST.setCategories(List.of(1L, 2L));


        // --- Data Dummy untuk UNCONVERTED_UNSANITIZED_REQUEST ---
        String rawMarkdownTitle = "Judul <script>alert('XSS di title!');</script> *Test*";
        String rawMarkdownContent =
                "Ini adalah **konten utama**.<br>" +
                        "Dengan link berbahaya: <a href=\"javascript:alert('XSS Link');\">Klik</a>.<br>" +
                        "Gambar injeksi: <img src=\"x\" onerror=\"alert('XSS Gambar');\"><br>" +
                        "Dan tag tidak diizinkan: <iframe src=\"http://malicious.com\"></iframe> <marquee>Scroll</marquee><br>" +
                        "Ini Markdown:\n* Item 1\n* Item 2";
        String rawMarkdownExcerpt = "Excerpt pendek <img src=\"non-existent\" onload=\"alert('XSS');\"> *test*.";
        String rawImageUrl = "http://evil.com/image.jpg?param=<script>alert('url-xss')</script>";

        UNCONVERTED_UNSANITIZED_REQUEST = PostRequestDto.builder()
                .title(rawMarkdownTitle)
                .slug(Optional.of("judul-test"))
                .content(rawMarkdownContent)
                .excerpt(Optional.of(rawMarkdownExcerpt))
                .featuredImageUrl(Optional.of(rawImageUrl))
                .status(Optional.of(Status.DRAFT))
                .categories(Optional.of(List.of(1L, 2L)))
                .build();

        // --- Data Dummy untuk CONVERTED_UNSANITIZED_REQUEST ---
        // Konten sudah dalam bentuk HTML dari commonmark-java, belum disanitasi.
        String convertedHtmlTitle = "<p>Judul <script>alert('XSS di title!');</script> <em>Test</em></p>\n";
        String convertedHtmlContent =
                "<p>Ini adalah <strong>konten utama</strong>.<br>" +
                        "Dengan link berbahaya: <a href=\"javascript:alert('XSS Link');\">Klik</a>.<br>" +
                        "Gambar injeksi: <img src=\"x\" onerror=\"alert('XSS Gambar');\"><br>" +
                        "Dan tag tidak diizinkan: <iframe src=\"http://malicious.com\"></iframe> <marquee>Scroll</marquee><br>" +
                        "Ini Markdown:</p>\n" + "<ul>\n<li>Item 1</li>\n<li>Item 2</li>\n</ul>\n";
        String convertedHtmlExcerpt = "<p>Excerpt pendek <img src=\"non-existent\" onload=\"alert('XSS');\"> <em>test</em>.</p>\n";
        String convertedImageUrl = "http://evil.com/image.jpg?param=<script>alert('url-xss')</script>";


        CONVERTED_UNSANITIZED_REQUEST = PostRequestDto.builder()
                .title(convertedHtmlTitle)
                .slug(Optional.of("judul-test"))
                .content(convertedHtmlContent)
                .excerpt(Optional.of(convertedHtmlExcerpt))
                .featuredImageUrl(Optional.of(convertedImageUrl))
                .status(Optional.of(Status.DRAFT))
                .categories(Optional.of(List.of(1L, 2L)))
                .build();


        CONVERTED_SANITIZED_REQUEST = PostRequestDto.builder()
                .title("Judul  <em>Test</em>") // Tetap seperti ini (asumsi masalah title sudah beres, lihat bawah)
                .slug(Optional.of("judul-test"))
                .content(
                        "<p>Ini adalah <strong>konten utama</strong>.<br />Dengan link berbahaya: Klik.<br />Gambar injeksi: <br />Dan tag tidak diizinkan:  Scroll<br />Ini Markdown:</p>\n" +
                                "<ul><li>Item 1</li><li>Item 2</li></ul>\n"
                ) // <--- KOREKSI DI SINI: src="null" alt="" diubah menjadi <img />
                .excerpt(Optional.of(
                        "<p>Excerpt pendek  <em>test</em>.</p>\n"
                )) // <--- KOREKSI DI SINI: src="null" alt="" diubah menjadi <img />
                .featuredImageUrl(Optional.of("http://evil.com/image.jpg?param&#61;")) // Tetap seperti ini (asumsi masalah URL sudah beres, lihat bawah)
                .status(Optional.of(Status.DRAFT))
                .categories(Optional.of(List.of(1L, 2L)))
                .build();
    }
}