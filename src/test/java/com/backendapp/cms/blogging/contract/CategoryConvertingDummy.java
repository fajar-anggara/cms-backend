package com.backendapp.cms.blogging.contract;

import com.backendapp.cms.blogging.contract.bonded.AuthenticatedUserDummy;
import com.backendapp.cms.blogging.dto.CategoryRequestDto;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.openapi.dto.CategoryRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CategoryConvertingDummy {

    public static CategoryEntity CATEGORY_ENTITY;
    public static CategoriesSimpleDTO CATEGORIES_SIMPLE_RESPONSE;

    static {
        CATEGORY_ENTITY = CategoryEntity.builder()
                .id(1L)
                .name("cat1")
                .slug("cat1")
                .description("cat1")
                .user(AuthenticatedUserDummy.BLOGGER_USER)
                .posts(null)
                .createdAt(LocalDateTime.of(2025, 7, 10, 19, 45, 23))
                .updatedAt(LocalDateTime.of(2025, 7, 10, 19, 45, 23))
                .build();

        CATEGORIES_SIMPLE_RESPONSE = new CategoriesSimpleDTO();
        CATEGORIES_SIMPLE_RESPONSE.setId(1L);
        CATEGORIES_SIMPLE_RESPONSE.setName("cat1");
        CATEGORIES_SIMPLE_RESPONSE.setSlug("cat1");
    }

    public static CategoryRequest CATEGORY_RAW_REQUEST;
    public static CategoryRequestDto CATEGORY_CONVERTED;

    public static CategoryRequestDto CATEGORY_REQUEST_ASK_FOR_SAVED;

    static {
        CATEGORY_RAW_REQUEST = new CategoryRequest();
        CATEGORY_RAW_REQUEST.setName("Category Raw");
        CATEGORY_RAW_REQUEST.setDescription("Category Raw");
        CATEGORY_RAW_REQUEST.setSlug("Category Raw");
        CATEGORY_RAW_REQUEST.setPosts(List.of(1L));

        CATEGORY_CONVERTED = new CategoryRequestDto();
        CATEGORY_CONVERTED.setName(Optional.of("Category Raw"));
        CATEGORY_CONVERTED.setSlug(Optional.of("Category Raw"));
        CATEGORY_CONVERTED.setDescription(Optional.of("Category Raw"));
        CATEGORY_CONVERTED.setPosts(Optional.of(List.of(1L)));
    }
}
