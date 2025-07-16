package com.backendapp.cms.blogging.dto;


import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Nama kategori tidak boleh kosong")
    String name;
    Optional<String> description = Optional.empty();
    Optional<List<Long>> posts = Optional.empty();
}

