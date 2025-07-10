package com.backendapp.cms.blogging.helper;

import com.backendapp.cms.blogging.contract.CategorySanitizerContract;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CategorySanitizerTest {

    private final CategorySanitizer categorySanitizer = new CategorySanitizer();

    @Test
    @DisplayName("Category name sanitizer should be functional")
    void sanitizeName_shouldBeFunctional() {
        String unSanitizedName = CategorySanitizerContract.UNSANITIZED_NAME;
        String sanitizedName = CategorySanitizerContract.SANITIZED_NAME;

        String actual = categorySanitizer.sanitizeName(unSanitizedName);

        assertEquals(actual, sanitizedName);
    }

    @Test
    @DisplayName("Category slug sanitizer should be funtional")
    void sanitizeSlug_shouldBeFunctional() {
        String unSanitiserSlug = CategorySanitizerContract.UNSANITIZED_SLUG;
        String sanitizedSlug = CategorySanitizerContract.SANITIZED_SLUG;

        String actual = categorySanitizer.sanitizeSlug(unSanitiserSlug);

        assertEquals(actual, sanitizedSlug);
    }
}
