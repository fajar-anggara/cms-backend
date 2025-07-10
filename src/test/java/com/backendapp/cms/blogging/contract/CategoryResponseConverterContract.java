package com.backendapp.cms.blogging.contract;

import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.openapi.dto.CategorySimpleResponse;

import java.time.LocalDateTime;

public class CategoryResponseConverterContract {

    public static CategoryEntity CATEGORY_ENTITY;
    public static CategoriesSimpleDTO CATEGORIES_SIMPLE_RESPONSE;

    static {
        CATEGORY_ENTITY = CategoryEntity.builder()
                .id(1L)
                .name("cat1")
                .slug("cat1")
                .description("cat1")
                .user(AuthenticatedUserContract.BLOGGER_USER)
                .posts(null)
                .createdAt(LocalDateTime.of(2025, 7, 10, 19, 45, 23))
                .updatedAt(LocalDateTime.of(2025, 7, 10, 19, 45, 23))
                .build();

        CATEGORIES_SIMPLE_RESPONSE = new CategoriesSimpleDTO();
        CATEGORIES_SIMPLE_RESPONSE.setId(1L);
        CATEGORIES_SIMPLE_RESPONSE.setName("cat1");
        CATEGORIES_SIMPLE_RESPONSE.setSlug("cat1");
    }
}
