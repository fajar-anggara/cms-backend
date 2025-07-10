package com.backendapp.cms.blogging.converter;


import com.backendapp.cms.blogging.converter.mapper.PostResponseMapper;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.openapi.dto.PostSimpleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = PostResponseMapper.class)
public interface PostResponseConverter {

    @Mapping(source = "categories", target = "categories", qualifiedByName = "mapFromSetCategoriesEntityToListCategoriesSimpleDto")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "mapFromLocalDateTimeToOffsetDateTime")
    @Mapping(source = "publishedAt", target = "publishedAt", qualifiedByName = "mapFromLocalDateTimeToOffsetDateTime")
    @Mapping(source = "user", target = "user", ignore = true)
    PostSimpleResponse fromPostEntityToPostSimpleResponse(PostEntity postEntity);

}