package com.backendapp.cms.blogging.converter;

import com.backendapp.cms.blogging.dto.CategoryRequestDto;
import com.backendapp.cms.openapi.dto.CategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryRequestConverter {

    public CategoryRequestDto fromCategoryRequestToCategoryRequestDto(CategoryRequest categoryRequest);

    default Optional<String> mapFromStringToOptionalString(String input) {
        return Optional.ofNullable(input);
    }

    default Optional<List<Long>> mapFromListOfLongToOptionalListOfLong(List<Long> input) {
        if (input.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(input);
        }
    }
}
