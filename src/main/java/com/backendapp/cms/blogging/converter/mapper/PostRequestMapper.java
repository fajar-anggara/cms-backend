package com.backendapp.cms.blogging.converter.mapper;

import com.backendapp.cms.common.enums.Status;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.openapi.dto.PostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class PostRequestMapper {

    @Named("mapFromStringToOptionalString")
    public Optional<String> mapFromStringToOptionalString(String input) {
        return Optional.ofNullable(input);
    }

    @Named("mapFromStatusEnumToStatusEnum")
    public Optional<Status> mapFromStatusEnumToOptionalStatusEnum(PostRequest.StatusEnum input) {
        return Optional.of(Status.valueOf(input.name()));
    }

    @Named("mapFromListOfCategoriesSimpleDtoToOptionalListOfCategoriesSimpleDto")
    public Optional<List<CategoriesSimpleDTO>> mapFromListOfLongToOptionalListOfLong(List<CategoriesSimpleDTO> input) {
        return Optional.ofNullable(input);
    }
}
