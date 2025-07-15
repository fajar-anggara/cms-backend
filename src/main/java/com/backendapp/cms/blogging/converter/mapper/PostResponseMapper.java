package com.backendapp.cms.blogging.converter.mapper;

import com.backendapp.cms.blogging.converter.CategoryResponseConverter;
import com.backendapp.cms.blogging.converter.CategoryResponseConverterImpl;
import com.backendapp.cms.blogging.converter.SharedPostAndCategoryConverter;
import com.backendapp.cms.blogging.converter.SharedPostAndCategoryConverterImpl;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.common.enums.Status;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.openapi.dto.PostRequest;
import com.backendapp.cms.openapi.dto.UserSimpleResponse;
import com.backendapp.cms.security.entity.UserGrantedAuthority;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mapper(componentModel = "spring")
@AllArgsConstructor
public abstract class PostResponseMapper {

    private final SharedPostAndCategoryConverter sharedPostAndCategoryConverter = new SharedPostAndCategoryConverterImpl();

    @Named("mapFromSetCategoriesEntityToListCategoriesSimpleDto")
    public List<CategoriesSimpleDTO> mapFromSetCategoriesEntityToListCategoriesSimpleDto (Set<CategoryEntity> set) {
        return set
                .stream()
                .map(sharedPostAndCategoryConverter::fromCategoriesEntityToCategorySimpleDto)
                .toList();
    }

    @Named("mapFromPostEntityUserAuthorityToPostSimpleResponseUserAuthorityEnum")
    public UserSimpleResponse.AuthorityEnum mapFromPostEntityUserAuthorityToPostSimpleResponseUserAuthorityEnum(UserGrantedAuthority authority) {
        return UserSimpleResponse.AuthorityEnum.valueOf(authority.getAuthority().substring(5));
    }
}
