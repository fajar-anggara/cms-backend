package com.backendapp.cms.blogging.converter;

import com.backendapp.cms.blogging.converter.mapper.PostRequestMapper;
import com.backendapp.cms.blogging.dto.PostRequestDto;
import com.backendapp.cms.openapi.dto.PostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = PostRequestMapper.class)
public interface PostRequestConverter {

    @Mapping(source = "slug", target = "slug", qualifiedByName = "mapFromStringToOptionalString")
    @Mapping(source = "excerpt", target = "excerpt", qualifiedByName = "mapFromStringToOptionalString")
    @Mapping(source = "featuredImageUrl", target = "featuredImageUrl", qualifiedByName = "mapFromStringToOptionalString")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapFromStatusEnumToStatusEnum")
    @Mapping(source = "categories", target = "categories", qualifiedByName = "mapFromListOfCategoriesSimpleDtoToOptionalListOfCategoriesSimpleDto")
    PostRequestDto fromPostRequestToPostRequestDto(PostRequest postRequest);


}
