package com.backendapp.cms.blogging.contract.bonded;

import com.backendapp.cms.blogging.dto.CategoryRequestDto;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.users.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.*;

public class CategoryGenerator_PostArticleOperationPerformerDummy {
    public static List<String> LIST_OF_CATEGORIES_NAME;
    public static Collection<String> COLLECTION_OF_CATEGORIES_NAME;
    public static Set<CategoryEntity> SET_OF_AVAILABLE_CATEGORIES;
    public static Set<CategoryEntity> SET_OF_UNAVAILABLE_UNSAVED_CATEGORIES;
    public static List<CategoryEntity> LIST_OF_UNAVAILABLE_SAVED_CATEGORIES;
    public static Set<CategoryEntity> SET_OF_CATEGORY_AVAILABLE_AND_UNAVAILABLE_SAVED;

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
                .user(AuthenticatedUserDummy.BLOGGER_USER)
                .posts(null)
                .createdAt(LocalDateTime.of(2025, 7, 10, 19, 45, 23))
                .updatedAt(LocalDateTime.of(2025, 7, 10, 19, 45, 23))
                .build();
        CategoryEntity catEntity2 = CategoryEntity.builder()
                .id(2L)
                .name("cat2")
                .slug("cat2")
                .description("cat2")
                .user(AuthenticatedUserDummy.BLOGGER_USER)
                .posts(null)
                .createdAt(LocalDateTime.of(2025, 7, 10, 19, 45, 23))
                .updatedAt(LocalDateTime.of(2025, 7, 10, 19, 45, 23))
                .build();
        CategoryEntity catEntity3 = CategoryEntity.builder()
                .id(3L)
                .name("cat3")
                .slug("cat3")
                .description("cat3")
                .user(AuthenticatedUserDummy.BLOGGER_USER)
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

    public static PostEntity POST_ENTITY;
    public static Set<CategoryEntity> categories = CategoryGenerator_PostArticleOperationPerformerDummy.SET_OF_AVAILABLE_CATEGORIES;
    public static UserEntity user = AuthenticatedUserDummy.BLOGGER_USER;

    static {
        POST_ENTITY = new PostEntity();
        POST_ENTITY.setId(1L);
        POST_ENTITY.setTitle("Cara membuat aplikasi cms dengan springboot");
        POST_ENTITY.setContent("<h1>Cara membuat aplikasi cms dengan springboot</h1><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>");
        POST_ENTITY.setExcerpt("<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>\n");
        POST_ENTITY.setSlug("cara-membuat-aplikasi-cms-dengan-springboot");
        POST_ENTITY.setCategories(categories);
        POST_ENTITY.setFeaturedImageUrl("https://www.google.com/imgres?q&#61;images%20link&amp;imgurl&#61;https%3A%2F%2Fstatic.wikia.nocookie.net%2Fversus-compendium%2Fimages%2F0%2F00%2FLink_BotW.png%2Frevision%2Flatest%3Fcb%3D20181128185543&amp;imgrefurl&#61;https%3A%2F%2Fversus-compendium.fandom.com%2Fwiki%2FLink_(Breath_of_the_Wild)&amp;docid&#61;6rbDPZVj4wZe7M&amp;tbnid&#61;R9iU04UWo8X02M&amp;vet&#61;12ahUKEwiD9MWtr-uNAxVe4jgGHWUnPH8QM3oECGgQAA..i&amp;w&#61;600&amp;h&#61;600&amp;hcb&#61;2&amp;ved&#61;2ahUKEwiD9MWtr-uNAxVe4jgGHWUnPH8QM3oECGgQAA");
        POST_ENTITY.setUser(user);
        POST_ENTITY.setCreatedAt(LocalDateTime.of(2025, 7, 10, 19, 45, 23));
        POST_ENTITY.setPublishedAt(LocalDateTime.of(2025, 7, 10, 19, 45, 23));
    }
}