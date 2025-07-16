package com.backendapp.cms.blogging.contract;

import com.backendapp.cms.blogging.dto.CategoryRequestDto;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.openapi.dto.*;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.persistence.ManyToOne;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;

public class CategoryBuilder {

    private Long id = 1L;
    private String name;
    private String slug;
    private String description;
    @ManyToOne
    private UserEntity user;
    private Set<PostEntity> posts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CategoryBuilder withDefault() {
        this.id = 1L;
        this.name =  "Category";
        this.slug = "category";
        this.description =  "This is a category";
        this.user = new UserBuilder().withDefault().build();
        this.posts = new HashSet<>();
        this.createdAt = LocalDateTime.of(2025, 7, 10, 19, 45, 23);
        this.updatedAt = LocalDateTime.of(2025, 7, 10, 19, 45, 23);

        return this;
    }

    public CategoryBuilder withEmptyName() {
        this.name = "";
        return this;
    }

    public CategoryBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CategoryBuilder withEmptySlug() {
        this.slug = "";
        return this;
    }

    public CategoryBuilder withXss() {
        this.name = "<script>alert('XSS Attack!');</script>";
        this.slug = "xss-attack"; // Slug biasanya disanitasi jadi tidak bisa mengandung XSS
        this.description = "<img src=x onerror=alert('ImageXSS');>Dangerous Description";
        return this;
    }

    public CategoryBuilder withLongName() {
        this.name = "a".repeat(256);
        return this;
    }

    public CategoryBuilder withNullName() {
        this.name = null;
        return this;
    }

    public CategoryBuilder withPosts(Set<PostEntity> posts) {
        this.posts = posts;
        return this;
    }

    public CategoryEntity build() {
        return CategoryEntity.builder()
                .id(this.id)
                .name(this.name)
                .slug(this.slug)
                .description(this.description)
                .user(this.user)
                .posts(this.posts)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

    /**
     * @param count = jumlah List yang diinginkan
     * @return Jika count nya 1 maka id akan sama dengan withDefaiult
     */
    public List<CategoryEntity> buildList(int count) {
        Long currId = this.id;
        List<CategoryEntity> categories = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            CategoryEntity entity = CategoryEntity.builder()
                    .id(currId)
                    .name(this.name + i)
                    .slug(this.slug != null ? this.slug : null)
                    .description(this.description + i)
                    .user(this.user)
                    .createdAt(this.createdAt)
                    .updatedAt(this.updatedAt)
                    .build();
            currId++;

            categories.add(entity);
        }
        return categories;
    }

    /**
     * @param count = jumlah List yang diinginkan
     * @return Jika count nya 1 maka id akan sama dengan withDefaiult
     */
    public Set<CategoryEntity> buildSet(int count) {
        Long currId = this.id;
        Set<CategoryEntity> categories = new HashSet<>();
        for (int i = 0; i < count; i++) {
            CategoryEntity entity = CategoryEntity.builder()
                    .id(currId)
                    .name(this.name + i)
                    .slug(this.slug != null ? this.slug : null)
                    .description(this.description + i)
                    .user(this.user)
                    .createdAt(this.createdAt)
                    .updatedAt(this.updatedAt)
                    .build();
            currId++;

            categories.add(entity);
        }
        return categories;
    }

    public CategoryEntity buildXssExpectedResult() {
        String expectedSanitizedName = "";
        String expectedSanitizedSlug = "xss-attack-with-payload";
        String expectedSanitizedDescription = "Dangerous Description with <b>bold</b> text."; // Jika img dan script dihapus, b dipertahankan

        return CategoryEntity.builder()
                .id(this.id)
                .name(expectedSanitizedName)
                .slug(expectedSanitizedSlug)
                .description(expectedSanitizedDescription)
                .user(this.user)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

    public CategoriesSimpleDTO buildCategoriesSimpleDTO() {
        CategoriesSimpleDTO categoriesDTO = new CategoriesSimpleDTO();
        categoriesDTO.setId(this.id);
        categoriesDTO.setName(this.name);
        categoriesDTO.setSlug(this.slug);

        return categoriesDTO;
    }

    public CategoryRequest buildCategoryRequest() {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName(this.name);
        categoryRequest.setSlug(this.slug);
        categoryRequest.setDescription(this.description);
        // untuk nanti kedepannya tambahkan POST

        return categoryRequest;
    }

    public CategoryRequestDto buildCategoryRequestDto() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName(this.name);
        categoryRequestDto.setSlug(Optional.of(this.slug));
        categoryRequestDto.setDescription(Optional.of(this.description));
        // nanti kedepannya tambahkan POST

        return categoryRequestDto;
    }

    public CategoryResponse buildCategoryResponse () {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(this.id);
        categoryResponse.setName(this.name);
        categoryResponse.setSlug(this.slug);
        categoryResponse.setUser(new UserBuilder().withDefault().buildUserSimpleResponse());
        categoryResponse.setDescription(this.description);
        categoryResponse.createdAt(this.createdAt.atZone(ZoneId.systemDefault()).toOffsetDateTime());

        return categoryResponse;
    }

    /**
     * @param count = jumlah List yang diinginkan
     * @return Jika count nya 1 maka id akan sama dengan withDefaiult
     */
    public List<CategoriesSimpleDTO> buildListCategoriesSimpleDTO(int count) {
        Long currId = this.id;
        List<CategoriesSimpleDTO> categories = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            CategoriesSimpleDTO category = new CategoriesSimpleDTO();
            category.setId(currId);
            category.setName(this.name + i);
            category.setSlug(this.slug != null ? this.slug : null);
            currId++;

            categories.add(category);
        }

        return categories;
    }

    public Set<CategoriesSimpleDTO> buildSetCategoriesSimpleDTO(int count) {
        Long currId = this.id;
        Set<CategoriesSimpleDTO> categories = new HashSet<>();
        for (int i = 0; i < count; i++) {
            CategoriesSimpleDTO category = new CategoriesSimpleDTO();
            category.setId(currId);
            category.setName(this.name + i);
            category.setSlug(this.slug != null ? this.slug : null);
            currId++;

            categories.add(category);
        }

        return categories;
    }
}
