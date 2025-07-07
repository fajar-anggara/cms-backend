package com.backendapp.cms.blogging.converter.mapper;

import com.backendapp.cms.common.enums.Status;
import com.backendapp.cms.openapi.dto.PostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostStatusMapper {

    @Named("mapPostRequestStatusEnumToPostRequestDtoStatusEnum")
    Optional<Status> mapPostRequestStatusEnumToPostRequestDtoStatusEnum(PostRequest.StatusEnum status) {
        return Optional.of(Status.valueOf(status.name()));
    }
}
