package com.backendapp.cms.blogging.dto;


import com.backendapp.cms.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@UniqueSlug
@Builder
public class PostRequestDto {
    String title;
    Optional<String> slug = Optional.empty();
    String content;
    Optional<String> excerpt = Optional.empty();
    Optional<String> featuredImageUrl = Optional.empty();
    Optional<Status> status = Optional.empty();
    Optional<Set<Long>> categories = Optional.empty();

}

