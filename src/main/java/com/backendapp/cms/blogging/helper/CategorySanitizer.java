package com.backendapp.cms.blogging.helper;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Component;

@Component
public class CategorySanitizer {

    private final PolicyFactory plainText = new HtmlPolicyBuilder().toFactory();

    public String toPlainText(String name) {
        return plainText.sanitize(name);
    }

    public String sanitizeSlug(String slug) {
        return slug.toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "")
                .replaceAll("-+", "-");
    }
}
