package com.backendapp.cms.users.converter;

import com.backendapp.cms.openapi.dto.CreateUserRequest;
import com.backendapp.cms.openapi.dto.UserRegisterRequest;
import com.backendapp.cms.users.converter.mapper.UserRegisterMapper;
import com.backendapp.cms.users.dto.UserRegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = UserRegisterMapper.class)
public interface UserRegisterConverter {

    @Mapping(target = "authority", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    UserRegisterDto fromUserRegisterRequestToUserRegisterDto(UserRegisterRequest userRegisterRequest);

    @Mapping(target = "authority", source = "authority", qualifiedByName = "mapCreateUserRequestAuthorityEnumToCommonEnumAuthorityEnum")
    UserRegisterDto fromCreateUserRequestToUserRegisterDto(CreateUserRequest createUserRequest);

    default Optional<String> map (String value) {
        return Optional.ofNullable(value);
    }
}
