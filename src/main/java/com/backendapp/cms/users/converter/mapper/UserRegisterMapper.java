package com.backendapp.cms.users.converter.mapper;

import com.backendapp.cms.common.enums.Authority;
import com.backendapp.cms.openapi.dto.CreateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public class UserRegisterMapper {

    @Named("mapCreateUserRequestAuthorityEnumToCommonEnumAuthorityEnum")
    public Authority mapCreateUserRequestAuthorityEnumToCommonEnumAuthorityEnum(CreateUserRequest.AuthorityEnum authorityEnum) {
        return Authority.valueOf(authorityEnum.name());
    }
}
