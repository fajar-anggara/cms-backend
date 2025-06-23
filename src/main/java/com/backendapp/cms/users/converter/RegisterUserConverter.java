package com.backendapp.cms.users.converter;

import com.backendapp.cms.openapi.dto.UserDTO;
import com.backendapp.cms.openapi.dto.UserRegisterRequest;
import com.backendapp.cms.openapi.dto.UserSimpleResponse;
import com.backendapp.cms.users.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterUserConverter {

    UserDTO toDto(UserEntity userEntity);

    @Mapping(source = "")
    UserEntity toEntity(UserRegisterRequest userRegisterRequest);
    UserSimpleResponse toSimpleResponse(UserEntity userEntity);

}
