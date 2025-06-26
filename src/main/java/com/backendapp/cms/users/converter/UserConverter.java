package com.backendapp.cms.users.converter;

import com.backendapp.cms.openapi.dto.UserRegisterRequest;
import com.backendapp.cms.openapi.dto.UserResponse;
import com.backendapp.cms.openapi.dto.UserSimpleResponse;
import com.backendapp.cms.users.converter.mapper.UserAuthorityMapper;
import com.backendapp.cms.users.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserAuthorityMapper.class})
public interface UserConverter {

    // UserDTO toDto(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "profilePicture", ignore = true)
    @Mapping(target = "bio", ignore = true)
    @Mapping(target = "authority", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "isEmailVerified", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    UserEntity toEntity(UserRegisterRequest userRegisterRequest);

    @Mapping(source = "authority", target = "authority", qualifiedByName = "mapUserGrantedAuthorityToUserSimpleResponseAuthorityEnum")
    UserSimpleResponse toSimpleResponse(UserEntity userEntity);

    @Mapping(source = "authority", target = "authority", qualifiedByName = "mapUserGrantedAuthorityToUserResponseAuthorityEnum")
    UserResponse toUserResponse(UserEntity userEntity);
}


