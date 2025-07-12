package com.backendapp.cms.blogging.converter;

import com.backendapp.cms.blogging.dto.CategoryRequestDto;
import com.backendapp.cms.openapi.dto.CategoryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CategoryRequestConverterTest {

    private final CategoryRequestConverter categoryRequestConverter = new CategoryRequestConverterImpl();

    @Test
    @DisplayName("Category convert from categoryRequest to CategoryRequestDto should be functional")
    void fromCategoryRequestToCategoryRequestDto_shouldBeFunctional() {
        CategoryRequest unmapped = CategoryRequestConverterContract.CATEGORY_RAW_REQUEST;
        CategoryRequestDto mapped = CategoryRequestConverterContract.CATEGORY_CONVERTED;

        CategoryRequestDto actual = categoryRequestConverter.fromCategoryRequestToCategoryRequestDto(unmapped);

        assertEquals(actual, mapped);
    }
}
