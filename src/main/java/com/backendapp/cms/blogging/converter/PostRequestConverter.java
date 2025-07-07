package com.backendapp.cms.blogging.converter;

import com.backendapp.cms.blogging.dto.PostRequestDto;
import com.backendapp.cms.common.enums.Status;
import com.backendapp.cms.openapi.dto.PostRequest;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface PostRequestConverter {

    PostRequestDto fromPostRequestToPostRequestDto(PostRequest postRequest);

    default Optional<String> map1(String value) {
        return Optional.ofNullable(value);
    }

    default Optional<List<Long>> map2(List<Long> value) {
        return Optional.ofNullable(value);
    }

    default Optional<Status> mapPostRequestStatusEnumToPostRequestDtoStatusEnum(PostRequest.StatusEnum status) {
        return Optional.of(Status.valueOf(status.name()));
    }

}
