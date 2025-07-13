package com.backendapp.cms.blogging.converter;

import com.backendapp.cms.blogging.contract.CategoryBuilder;
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
    @DisplayName("Should return CategoryRequestDto when received CategoryRequest")
    void fromCategoryRequestToCategoryRequestDto_shouldBeFunctional() {
        CategoryRequest unmapped = new CategoryBuilder().withDefault().buildCategoryRequest();
        CategoryRequestDto mapped = new CategoryBuilder().withDefault().buildCategoryRequestDto();

        CategoryRequestDto actual = categoryRequestConverter.fromCategoryRequestToCategoryRequestDto(unmapped);

        assertEquals(actual, mapped);
    }
}
