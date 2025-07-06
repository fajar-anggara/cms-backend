package com.backendapp.cms.blogging.helper;

import com.backendapp.cms.blogging.repository.PostCrudRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@AllArgsConstructor
public class PostGenerator {

    private final PostCrudRepository postCrudRepository;
    private static final int DEFAULT_EXCERPT_LENGTH = 150;

    /**
     * Generate unique slug from title
     * @param title the post title
     * @return unique slug
     */
    public String generateSlug(String title) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        String baseSlug = baseSlug(title);
        if (!postCrudRepository.existsBySlug(baseSlug)) {
            return baseSlug;
        }

        String uniqueSlug;
        do {
            uniqueSlug = baseSlug + "-" + UUID.randomUUID().toString().substring(0, 8);
        } while (postCrudRepository.existsBySlug(uniqueSlug));

        return uniqueSlug;
    }

    public String generateExcerpt(String content) {
        return generateExcerpt(content, DEFAULT_EXCERPT_LENGTH);
    }

    public String generateExcerpt(String content, int maxLength) {
        if (!StringUtils.hasText(content)) {
            return "";
        }

        if (maxLength <= 0) {
            throw new IllegalArgumentException("Max length must be positive");
        }

        String cleanContent = content
                .replaceAll("<[^>]*>", "")  // Remove HTML tags
                .replaceAll("\\s+", " ")    // Replace multiple spaces with single space
                .trim();

        if (cleanContent.length() <= maxLength) {
            return cleanContent;
        }

        int lastSpaceIndex = cleanContent.lastIndexOf(' ', maxLength);
        if (lastSpaceIndex > 0) {
            return cleanContent.substring(0, lastSpaceIndex) + "...";
        }

        return cleanContent.substring(0, maxLength) + "...";
    }

    public LocalDateTime getPublishedAt() {
        return LocalDateTime.now();
    }
    private static String baseSlug(String title) {
        if (!StringUtils.hasText(title)) {
            return "";
        }

        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // Remove non-alphanumeric chars except spaces and hyphens
                .replaceAll("\\s+", "-")         // Replace spaces with hyphens
                .replaceAll("-+", "-")           // Replace multiple hyphens with single hyphen
                .replaceAll("^-|-$", "");        // Remove leading/trailing hyphens
    }
}