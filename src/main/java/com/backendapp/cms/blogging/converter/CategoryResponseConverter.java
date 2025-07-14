package com.backendapp.cms.blogging.converter;


import com.backendapp.cms.blogging.converter.mapper.CategoryResponseMapper;
import com.backendapp.cms.blogging.converter.mapper.PostResponseMapper;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.openapi.dto.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = CategoryResponseMapper.class)
public interface CategoryResponseConverter {

    CategoriesSimpleDTO fromCategoriesEntityToCategorySimpleDto(CategoryEntity category);

//    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "mapFromLocalDateTimeToOffsetDateTime")
//    @Mapping(source = "posts", target = "posts", qualifiedByName = "mapFromSetPostsToLisP")
//    CategoryResponse fromCategoryEntityToCategoryResponse(CategoryEntity category);
}
