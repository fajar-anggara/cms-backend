package com.backendapp.cms.users.converter;

import com.backendapp.cms.openapi.dto.UserUpdateRequest;
import com.backendapp.cms.users.dto.UserUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserUpdateConverter {
    UserUpdateDto fromUserUpdateRequestToUserUpdateDto(UserUpdateRequest userUpdateRequest);

    default Optional<String> map(String value) {
        return Optional.ofNullable(value);
    }

    default Optional<Boolean> map(Boolean value) {
        return Optional.ofNullable(value);
    }
}
