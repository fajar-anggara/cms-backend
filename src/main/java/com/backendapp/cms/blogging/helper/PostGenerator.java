package com.backendapp.cms.blogging.helper;

import com.backendapp.cms.blogging.repository.PostCrudRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class PostGenerator {

    private final PostCrudRepository postCrudRepository;
    private final CategorySanitizer categorySanitizer;
    private static final int DEFAULT_EXCERPT_LENGTH = 200;

    /**
     * Generate unique slug from title
     * @param title the post title
     * @return unique slug
     */
    public String generateSlug(Optional<String> slug, String title) {
        if (slug.isPresent() && !postCrudRepository.existsBySlug(categorySanitizer.sanitizeSlug(slug.get()))) {
            return categorySanitizer.sanitizeSlug(slug.get());
        }
        if (title != null && !postCrudRepository.existsBySlug(categorySanitizer.sanitizeSlug(title))) {
            return categorySanitizer.sanitizeSlug(title);
        }
        return UUID.randomUUID().toString();
    }

    /**
     * Generare excerpt from content
     * @param content the post content
     * @return excerpt
     */
    public String generateExcerpt(String content) {
        return generateExcerpt(content, DEFAULT_EXCERPT_LENGTH);
    }

    public String generateExcerpt(String content, int maxLength) {
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

    /**
     * Generating date now for default published at
     * @return local date time now
     */
    public LocalDateTime getPublishedAt() {
        return LocalDateTime.now();
    }
}