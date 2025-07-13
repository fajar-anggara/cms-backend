package com.backendapp.cms.blogging.converter;

import com.backendapp.cms.blogging.contract.CategoryBuilder;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CategoryResponseConverterTest {

    private final CategoriesResponseConverter categoriesResponseConverter = new CategoriesResponseConverterImpl();

    @Test
    @DisplayName("Should return CategorySimpleDto when received categoryEntity")
    void fromCategoriesEntityToCategorySimpleDto_shouldBeFunctional() {
        CategoryEntity categoryEntity = new CategoryBuilder().withDefault().build();
        CategoriesSimpleDTO categoriesSimpleDTO = new CategoryBuilder().withDefault().buildCategoriesSimpleDTO();

        CategoriesSimpleDTO actual = categoriesResponseConverter.fromCategoriesEntityToCategorySimpleDto(categoryEntity);

        assertEquals(actual, categoriesSimpleDTO);
    }
}
