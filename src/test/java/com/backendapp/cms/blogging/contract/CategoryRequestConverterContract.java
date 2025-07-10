package com.backendapp.cms.blogging.contract;

import com.backendapp.cms.blogging.dto.CategoryRequestDto;
import com.backendapp.cms.openapi.dto.CategoryRequest;

import java.util.List;
import java.util.Optional;

public class CategoryRequestConverterContract {

    public static CategoryRequest CATEGORY_RAW_REQUEST;
    public static CategoryRequestDto CATEGORY_CONVERTED;

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
