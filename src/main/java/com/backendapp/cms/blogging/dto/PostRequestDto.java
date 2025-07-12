package com.backendapp.cms.blogging.dto;


import com.backendapp.cms.common.enums.Status;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@UniqueSlug
@Builder
@EqualsAndHashCode
public class PostRequestDto {
    @NotBlank
    String title;
    Optional<String> slug = Optional.empty();
    @NotBlank
    String content;
    Optional<String> excerpt = Optional.empty();
    Optional<String> featuredImageUrl = Optional.empty();
    Optional<Status> status = Optional.empty();
    Optional<List<CategoriesSimpleDTO>> categories = Optional.empty();

}

