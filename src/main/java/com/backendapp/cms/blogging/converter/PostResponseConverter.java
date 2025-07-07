package com.backendapp.cms.blogging.converter;


import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.openapi.dto.PostSimpleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostResponseConverter {

    PostSimpleResponse fromPostEntityToPostSimpleResponse(PostEntity postEntity);

    default List<CategoriesSimpleDTO> fromSetCategoriesEntityToListCategoriesSimpleDto(Set<CategoryEntity> categoryEntities) {
        CategoriesResponseConverter categoriesResponseConverter = new CategoriesResponseConverterImpl();
        return categoryEntities.stream()
                .map(categoriesResponseConverter::fromCategoriesEntityToCategorySimpleDto)
                .toList();
    }

    default OffsetDateTime fromLocalDateTimeToOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime.atOffset(ZoneOffset.UTC);
    }
}