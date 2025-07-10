package com.backendapp.cms.blogging.converter.mapper;

import com.backendapp.cms.blogging.converter.CategoriesResponseConverter;
import com.backendapp.cms.blogging.converter.CategoriesResponseConverterImpl;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.openapi.dto.UserSimpleResponse;
import com.backendapp.cms.users.converter.UserResponseConverter;
import com.backendapp.cms.users.converter.UserResponseConverterImpl;
import com.backendapp.cms.users.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
@AllArgsConstructor
public abstract class PostResponseMapper {

    private final CategoriesResponseConverter categoriesResponseConverter = new CategoriesResponseConverterImpl();

    @Named("mapFromSetCategoriesEntityToListCategoriesSimpleDto")
    public List<CategoriesSimpleDTO> mapFromSetCategoriesEntityToListCategoriesSimpleDto (Set<CategoryEntity> set) {
        return set
                .stream()
                .map(categoriesResponseConverter::fromCategoriesEntityToCategorySimpleDto)
                .toList();
    }

    @Named("mapFromLocalDateTimeToOffsetDateTime")
    public OffsetDateTime mapFromLocalDateTimeToOffsetDateTime(LocalDateTime localDateTime) {
        ZoneId jakartaZone = ZoneId.of("Asia/Jakarta");
        return localDateTime.atZone(jakartaZone).toOffsetDateTime();
    }
}
