package com.backendapp.cms.blogging.contract;

import com.backendapp.cms.blogging.dto.CategoryRequestDto;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;

import java.time.LocalDateTime;
import java.util.*;

public class CategoryGeneratorContract {
    public static List<String> LIST_OF_CATEGORIES_NAME;
    public static Collection<String> COLLECTION_OF_CATEGORIES_NAME;
    public static Set<CategoryEntity> SET_OF_AVAILABLE_CATEGORIES;
    public static Set<CategoryEntity> SET_OF_UNAVAILABLE_UNSAVED_CATEGORIES;
    public static List<CategoryEntity> LIST_OF_UNAVAILABLE_SAVED_CATEGORIES;
    public static Set<CategoryEntity> SET_OF_CATEGORY_AVAILABLE_AND_UNAVAILABLE_SAVED;

    public static CategoryRequestDto CATEGORY_REQUEST_ASK_FOR_SAVED;
    public static CategoryEntity SAVED_CATEGORY;

    static {
        CategoriesSimpleDTO cat1 = new CategoriesSimpleDTO();
        cat1.setName("Cat1");
        CategoriesSimpleDTO cat2 = new CategoriesSimpleDTO();
        cat2.setName("Cat2");
        CategoriesSimpleDTO cat3 = new CategoriesSimpleDTO();
        cat3.setName("Cat3");
        CategoryEntity catEntity1 = CategoryEntity.builder()
                .id(1L)
                .name("cat1")
                .slug("cat1")
                .description("cat1")
                .user(AuthenticatedUserContract.BLOGGER_USER)
                .posts(null)
                .createdAt(LocalDateTime.of(2025, 7, 10, 19, 45, 23))
                .updatedAt(LocalDateTime.of(2025, 7, 10, 19, 45, 23))
                .build();
        CategoryEntity catEntity2 = CategoryEntity.builder()
                .id(2L)
                .name("cat2")
                .slug("cat2")
                .description("cat2")
                .user(AuthenticatedUserContract.BLOGGER_USER)
                .posts(null)
                .createdAt(LocalDateTime.of(2025, 7, 10, 19, 45, 23))
                .updatedAt(LocalDateTime.of(2025, 7, 10, 19, 45, 23))
                .build();
        CategoryEntity catEntity3 = CategoryEntity.builder()
                .id(3L)
                .name("cat3")
                .slug("cat3")
                .description("cat3")
                .user(AuthenticatedUserContract.BLOGGER_USER)
                .posts(null)
                .createdAt(LocalDateTime.of(2025, 7, 10, 19, 45, 23))
                .updatedAt(LocalDateTime.of(2025, 7, 10, 19, 45, 23))
                .build();

        LIST_OF_CATEGORIES_NAME = new ArrayList<>();
        LIST_OF_CATEGORIES_NAME.add("cat1");
        LIST_OF_CATEGORIES_NAME.add("cat2");
        LIST_OF_CATEGORIES_NAME.add("cat3");

        COLLECTION_OF_CATEGORIES_NAME = new ArrayList<>(LIST_OF_CATEGORIES_NAME); // Bisa juga new HashSet<>(...)

        SET_OF_AVAILABLE_CATEGORIES = new HashSet<>();
        SET_OF_AVAILABLE_CATEGORIES.add(catEntity1);
        SET_OF_AVAILABLE_CATEGORIES.add(catEntity2);

        SET_OF_UNAVAILABLE_UNSAVED_CATEGORIES = new HashSet<>();
        SET_OF_UNAVAILABLE_UNSAVED_CATEGORIES.add(catEntity3);

        LIST_OF_UNAVAILABLE_SAVED_CATEGORIES = new ArrayList<>();
        LIST_OF_UNAVAILABLE_SAVED_CATEGORIES.add(catEntity3);

        SET_OF_CATEGORY_AVAILABLE_AND_UNAVAILABLE_SAVED = new HashSet<>();
        SET_OF_CATEGORY_AVAILABLE_AND_UNAVAILABLE_SAVED.add(catEntity1);
        SET_OF_CATEGORY_AVAILABLE_AND_UNAVAILABLE_SAVED.add(catEntity2);
        SET_OF_CATEGORY_AVAILABLE_AND_UNAVAILABLE_SAVED.add(catEntity3);

        CATEGORY_REQUEST_ASK_FOR_SAVED = new CategoryRequestDto();
        CATEGORY_REQUEST_ASK_FOR_SAVED.setName(Optional.of(catEntity1.getName()));
        CATEGORY_REQUEST_ASK_FOR_SAVED.setSlug(Optional.of(catEntity1.getSlug()));
        CATEGORY_REQUEST_ASK_FOR_SAVED.setDescription(Optional.of(catEntity1.getDescription()));
        CATEGORY_REQUEST_ASK_FOR_SAVED.setPosts(Optional.empty());

        SAVED_CATEGORY = catEntity1;
    }
}