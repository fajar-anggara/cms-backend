package com.backendapp.cms.blogging.converter;

import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.openapi.dto.UserSimpleResponse;
import com.backendapp.cms.security.entity.UserGrantedAuthority;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SharedPostAndCategoryConverter {

    CategoriesSimpleDTO fromCategoriesEntityToCategorySimpleDto(CategoryEntity category);

    @Named("mapFromLocalDateTimeToOffsetDateTime")
    default OffsetDateTime mapFromLocalDateTimeToOffsetDateTime(LocalDateTime localDateTime) {
        ZoneId jakartaZone = ZoneId.of("Asia/Jakarta");
        return localDateTime.atZone(jakartaZone).toOffsetDateTime();
    }

    @Named("mapFromUserAuthorityToUserAuthorityEnum")
    default UserSimpleResponse.AuthorityEnum mapFromPostEntityUserAuthorityToPostSimpleResponseUserAuthorityEnum(UserGrantedAuthority authority) {
        return UserSimpleResponse.AuthorityEnum.valueOf(authority.getAuthority().substring(5));
    }
}
