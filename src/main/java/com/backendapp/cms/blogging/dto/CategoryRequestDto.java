package com.backendapp.cms.blogging.dto;


import com.backendapp.cms.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@UniqueSlug
@Builder
public class CategoryRequestDto {
    Optional<String> name = Optional.empty();
    Optional<String> slug = Optional.empty();
    Optional<String> description = Optional.empty();
    Optional<List<Long>> posts = Optional.empty();
}

