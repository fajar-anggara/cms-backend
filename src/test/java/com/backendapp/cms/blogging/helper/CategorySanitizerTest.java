package com.backendapp.cms.blogging.helper;

import com.backendapp.cms.blogging.contract.CategoryBuilder;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CategorySanitizerTest {

    private final CategorySanitizer categorySanitizer = new CategorySanitizer();

    @Test
    @DisplayName("Category name sanitizer should be functional")
    void toPlainText_shouldBeFunctional() {
        CategoryEntity category = new CategoryBuilder()
                .withDefault()
                .build();

        String actual = categorySanitizer.toPlainText(category.getName());

        assertEquals(actual, category.getName());
    }

    @Test
    @DisplayName("Category slug sanitizer should be funtional")
    void toSlugFormat_shouldBeFunctional() {
        CategoryEntity category = new CategoryBuilder()
                .withDefault()
                .withName("This is Category Name")
                .build();

        String actual = categorySanitizer.toSlugFormat(category.getName());

        assertEquals(actual, "this-is-category-name");
    }
}
