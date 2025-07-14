package com.backendapp.cms.blogging.converter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public class CategoryResponseMapper {

    @Named("mapFromLocalDateTimeToOffsetDateTime")
    public OffsetDateTime mapFromLocalDateTimeToOffsetDateTime(LocalDateTime localDateTime) {
        ZoneId jakartaZone = ZoneId.of("Asia/Jakarta");
        return localDateTime.atZone(jakartaZone).toOffsetDateTime();
    }
}
