package com.backendapp.cms.users.converter;

import com.backendapp.cms.openapi.dto.UserUpdateRequest;
import com.backendapp.cms.users.dto.UserUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserUpdateConverter {

    UserUpdateDto toUserUpdateDto(UserUpdateRequest userUpdateRequest);
}
