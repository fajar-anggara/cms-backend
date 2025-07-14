package com.backendapp.cms.blogging.converter.mapper;

import com.backendapp.cms.blogging.converter.CategoryResponseConverter;
import com.backendapp.cms.blogging.converter.CategoryResponseConverterImpl;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.openapi.dto.UserSimpleResponse;
import com.backendapp.cms.security.entity.UserGrantedAuthority;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
@AllArgsConstructor
public abstract class PostResponseMapper {

    private final CategoryResponseConverter categoryResponseConverter = new CategoryResponseConverterImpl();

    @Named("mapFromSetCategoriesEntityToListCategoriesSimpleDto")
    public List<CategoriesSimpleDTO> mapFromSetCategoriesEntityToListCategoriesSimpleDto (Set<CategoryEntity> set) {
        return set
                .stream()
                .map(categoryResponseConverter::fromCategoriesEntityToCategorySimpleDto)
                .toList();
    }

    @Named("mapFromLocalDateTimeToOffsetDateTime")
    public OffsetDateTime mapFromLocalDateTimeToOffsetDateTime(LocalDateTime localDateTime) {
        ZoneId jakartaZone = ZoneId.of("Asia/Jakarta");
        return localDateTime.atZone(jakartaZone).toOffsetDateTime();
    }

    @Named("mapFromPostEntityUserAuthorityToPostSimpleResponseUserAuthorityEnum")
    public UserSimpleResponse.AuthorityEnum mapFromPostEntityUserAuthorityToPostSimpleResponseUserAuthorityEnum(UserGrantedAuthority authority) {
        return UserSimpleResponse.AuthorityEnum.valueOf(authority.getAuthority().substring(5));
    }
}
