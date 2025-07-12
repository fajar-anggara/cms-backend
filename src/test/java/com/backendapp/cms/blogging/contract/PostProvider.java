package com.backendapp.cms.blogging.contract;

import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.openapi.dto.PostRequest;

import java.util.List;

public class PostProvider {

    public static final PostRequest UNCONVERTED_UNSANITIZED_RAWREQUEST;

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
        CategoriesSimpleDTO cat1 = new CategoriesSimpleDTO();
        cat1.setName("cat1");
        CategoriesSimpleDTO cat2 = new CategoriesSimpleDTO();
        cat2.setName("cat2");
        CategoriesSimpleDTO cat3 = new CategoriesSimpleDTO();
        cat3.setName("cat3");
        List<CategoriesSimpleDTO> categories = List.of(cat1, cat2, cat3);


        UNCONVERTED_UNSANITIZED_RAWREQUEST = new PostRequest();
        UNCONVERTED_UNSANITIZED_RAWREQUEST.setTitle(rawRequestTitle);
        UNCONVERTED_UNSANITIZED_RAWREQUEST.setSlug("judul-test");
        UNCONVERTED_UNSANITIZED_RAWREQUEST.setContent(rawRequestContent);
        UNCONVERTED_UNSANITIZED_RAWREQUEST.setExcerpt(rawRequestExcerpt);
        UNCONVERTED_UNSANITIZED_RAWREQUEST.setFeaturedImageUrl(rawRequestImageUrl);
        UNCONVERTED_UNSANITIZED_RAWREQUEST.setStatus(PostRequest.StatusEnum.DRAFT);
        UNCONVERTED_UNSANITIZED_RAWREQUEST.setCategories(categories);
    }
}
