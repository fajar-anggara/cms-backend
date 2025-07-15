package com.backendapp.cms.blogging.converter.mapper;

import com.backendapp.cms.blogging.converter.SharedPostAndCategoryConverter;
import com.backendapp.cms.blogging.converter.SharedPostAndCategoryConverterImpl;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.common.enums.Status;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.openapi.dto.CategoryResponseAllOfPosts;
import com.backendapp.cms.openapi.dto.PostRequest;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {
        SharedPostAndCategoryConverter.class
})
@AllArgsConstructor
public abstract class CategoryResponseMapper {

    private final SharedPostAndCategoryConverter sharedPostAndCategoryConverter = new SharedPostAndCategoryConverterImpl();

    @Named("mapFromSetCategoriesEntityToListCategoriesSimpleDto")
    public List<CategoriesSimpleDTO> mapFromSetCategoriesEntityToListCategoriesSimpleDto (Set<CategoryEntity> set) {
        return set
                .stream()
                .map(sharedPostAndCategoryConverter::fromCategoriesEntityToCategorySimpleDto)
                .toList();
    }

    @Named("mapFromStatusEnumToStatusEnumPrivate")
    public CategoryResponseAllOfPosts.StatusEnum mapFromStatusEnumToStatusEnumPrivate(Status input) {
        return CategoryResponseAllOfPosts.StatusEnum.valueOf(input.name());
    }
//
}
