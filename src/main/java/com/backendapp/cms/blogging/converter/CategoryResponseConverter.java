package com.backendapp.cms.blogging.converter;


import com.backendapp.cms.blogging.converter.mapper.CategoryResponseMapper;
import com.backendapp.cms.blogging.converter.mapper.PostResponseMapper;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.common.enums.Status;
import com.backendapp.cms.openapi.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {
        CategoryResponseMapper.class,
        SharedPostAndCategoryConverter.class,
})
public interface CategoryResponseConverter {

    CategoriesSimpleDTO fromCategoriesEntityToCategorySimpleDto(CategoryEntity category);

    GetAllCategories200ResponseAllOfData fromCategoryEntityToGetAllCategories200ResponseAllOfData(CategoryEntity categoryEntity);


    @Mapping(source = "categories", target = "categories", qualifiedByName = "mapFromSetCategoriesEntityToListCategoriesSimpleDto")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapFromStatusEnumToStatusEnumPrivate")
    @Mapping(source = "user.authority", target = "user.authority", qualifiedByName = "mapFromUserAuthorityToUserAuthorityEnum")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "mapFromLocalDateTimeToOffsetDateTime")
    @Mapping(source = "publishedAt", target = "publishedAt", qualifiedByName = "mapFromLocalDateTimeToOffsetDateTime")
    CategoryResponseAllOfPosts mapFromPostEntityToCategoryResponseAllOfPost(PostEntity post);

    @Mapping(source = "posts", target = "posts", ignore = true)
    @Mapping(source = "user.authority", target = "user.authority", qualifiedByName = "mapFromUserAuthorityToUserAuthorityEnum")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "mapFromLocalDateTimeToOffsetDateTime")
    CategoryResponse fromCategoryEntityToCategoryResponse(CategoryEntity category);
}
