package com.backendapp.cms.blogging.converter;

import com.backendapp.cms.blogging.contract.CategoryResponseConverterContract;
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
    @DisplayName("Categories response converter from CategoryEntity to CategorySimpleDto")
    void fromCategoriesEntityToCategorySimpleDto_shouldBeFunctional() {
        CategoryEntity categoryEntity = CategoryResponseConverterContract.CATEGORY_ENTITY;
        CategoriesSimpleDTO categoriesSimpleDTO = CategoryResponseConverterContract.CATEGORIES_SIMPLE_RESPONSE;

        CategoriesSimpleDTO actual = categoriesResponseConverter.fromCategoriesEntityToCategorySimpleDto(categoryEntity);

        assertEquals(actual, categoriesSimpleDTO);
    }
}
