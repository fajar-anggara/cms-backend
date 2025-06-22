package com.backendapp.cms.security.converter;

import com.backendapp.cms.openapi.dto.UserDTO;
import com.backendapp.cms.openapi.dto.UserRegisterRequest;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.validation.Valid;

public class RegistrationConverter {

    public static UserEntity toEntity(@Valid UserRegisterRequest request) {

        return UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .build();
    }

    public static UserDTO toDto(UserEntity userEntity) {

        return
    }
}
