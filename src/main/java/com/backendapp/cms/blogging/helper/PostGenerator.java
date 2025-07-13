package com.backendapp.cms.blogging.helper;

import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.blogging.repository.PostCrudRepository;
import com.backendapp.cms.common.constant.BloggingConstant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class PostGenerator {

    private final PostCrudRepository postCrudRepository;
    private final PolicyFactory plainText = new HtmlPolicyBuilder().toFactory();

    /**
     * Generate unique slug from title
     * @param title the post title
     * @return unique slug
     */
    public String generateSlug(Optional<String> slug, String title) {
        if (slug.isPresent()) {
            String presentSlug = plainText.sanitize(slug.get()).toLowerCase();
            String regexPattern = "^" + escapeRegex(presentSlug) + "(-[0-9]+)?$";
            List<PostEntity> findPostLikeSlug = postCrudRepository.findSlugPatternNative(presentSlug, regexPattern);
            if (findPostLikeSlug.isEmpty()) {
                return slugFormat(presentSlug);
            }

            log.warn("slug exists in database");
            int existsSlugs = findPostLikeSlug.size();


            return slugFormat(presentSlug + "-" + (existsSlugs + 1));
        }

        if (title != null) {
            String presentTitle = plainText.sanitize(title).toLowerCase();
            String regexPattern = "^" + escapeRegex(presentTitle) + "(-[0-9]+)?$";
            List<PostEntity> findPostLikeSlug = postCrudRepository.findSlugPatternNative(presentTitle, regexPattern);
            if (findPostLikeSlug.isEmpty()) {
                return slugFormat(presentTitle);
            }

            log.warn("slug exists in database");
            int existsSlugs = findPostLikeSlug.size();

            return slugFormat(presentTitle + "-" + (existsSlugs + 1));
        }

        return UUID.randomUUID().toString();
    }

    /**
     * Generate excerpt from content
     * @param content the post content
     * @return excerpt
     */
    public String generateExcerpt(String content) {
        return generateExcerpt(content, BloggingConstant.DEFAULT_EXCERPT_LENGTH);
    }

    public String generateExcerpt(String content, int maxLength) {
        String cleanContent = plainText.sanitize(content);
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

    private String escapeRegex(String input) {
        // Escape special regex characters untuk MySQL
        return input.replaceAll("([\\[\\](){}*+?^$|\\\\.])", "\\\\$1");
    }

    private String slugFormat(String slug) {
        return slug.toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "")
                .replaceAll("-+", "-");
    }
}