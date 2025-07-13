package com.backendapp.cms.blogging.dto;


import com.backendapp.cms.common.enums.Status;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.Objects;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostRequestDto that = (PostRequestDto) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(slug, that.slug) &&
                Objects.equals(content, that.content) &&
                Objects.equals(excerpt, that.excerpt) &&
                Objects.equals(featuredImageUrl, that.featuredImageUrl) &&
                Objects.equals(status, that.status) &&
                Objects.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, slug, content, excerpt, featuredImageUrl, status, categories);
    }
}

