package com.backendapp.cms.blogging.converter;


import com.backendapp.cms.openapi.dto.CategorySimpleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class CategoriesResponseConverter {

     fromCategoriesMuchEntityToCategorySimpleResponse(Category category) {}
}
