package com.backendapp.cms.blogging.converter;


import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoriesResponseConverter {

    CategoriesSimpleDTO fromCategoriesEntityToCategorySimpleDto(CategoryEntity category);
}
