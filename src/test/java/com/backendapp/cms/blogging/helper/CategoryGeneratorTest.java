package com.backendapp.cms.blogging.helper;


import com.backendapp.cms.blogging.contract.CategoryBuilder;
import com.backendapp.cms.blogging.contract.UserBuilder;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.repository.CategoryCrudRepository;
import com.backendapp.cms.users.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CategoryGeneratorTest {

    @MockitoBean
    CategoryCrudRepository categoryCrudRepository;

    @Autowired
    CategoryGenerator categoryGenerator;

    @Captor
    ArgumentCaptor<Set<CategoryEntity>> unavailableCategoriesCaptor;

    @Test
    @DisplayName("Should return all category names when no category name like that in database")
    void findOrCreateByName_shouldReturnAllCategoryNamesWhenNoCategoryNameLikeThatInDatabase() {
        Set<CategoryEntity> categoryEntities = new CategoryBuilder()
                .withDefault()
                .buildSet(3);

        List<CategoryEntity> categoryEntitiesList = categoryEntities.stream().toList();

        List<String> categoryNames = categoryEntities.stream()
                .map(CategoryEntity::getName)
                .toList();

        UserEntity user = new UserBuilder().withDefault().build();
        when(categoryCrudRepository.findAllByNameInAndUser(categoryNames, user))
                .thenReturn(new HashSet<>());
        when(categoryCrudRepository.saveAll(anySet()))
                .thenReturn(categoryEntitiesList);

        Set<CategoryEntity> actual = categoryGenerator.findOrCreateByName(categoryNames, user);

        verify(categoryCrudRepository).saveAll(unavailableCategoriesCaptor.capture());
        // yang mau di save harus 3
        assertEquals(categoryEntitiesList.size(), unavailableCategoriesCaptor.getValue().size());
        assertEquals(actual.size(), categoryNames.size());
    }

    @Test
    @DisplayName("Should return none in unavailable categories when the categories name is in database")
    void findOrCreateByName_shouldReturnNoneInUnavailableCategoriesWhenTheCategoriesNameIsInDatabase() {
        Set<CategoryEntity> categoryEntities = new CategoryBuilder()
                .withDefault()
                .buildSet(3);

        List<CategoryEntity> categoryEntitiesList = categoryEntities.stream().toList();

        List<String> categoryNames = categoryEntities.stream()
                .map(CategoryEntity::getName)
                .toList();

        UserEntity user = new UserBuilder().withDefault().build();
        when(categoryCrudRepository.findAllByNameInAndUser(categoryNames, user))
                .thenReturn(categoryEntities);
        when(categoryCrudRepository.saveAll(anySet()))
                .thenReturn(categoryEntitiesList);

        Set<CategoryEntity> actual = categoryGenerator.findOrCreateByName(categoryNames, user);

        verify(categoryCrudRepository).saveAll(unavailableCategoriesCaptor.capture());
        // yang mau di save harus 0
        assertEquals(0, unavailableCategoriesCaptor.getValue().size());
        assertEquals(actual.size(), categoryNames.size());
    }

    @Test
    @DisplayName("Should return both available and unavailable categories")
    void findOrCreateByName_shouldReturnBothAvailableAndUnavailableCategories() {
        Set<CategoryEntity> categoryEntitiesAvailableAndUnavailable = new CategoryBuilder()
                .withDefault()
                .buildSet(6);
        List<String> categoryEntitiesAvailableAndUnavailableNames = categoryEntitiesAvailableAndUnavailable
                .stream()
                .map(CategoryEntity::getName)
                .toList();

        Set<CategoryEntity> availableCategories = categoryEntitiesAvailableAndUnavailable.stream()
                .limit(3)
                .collect(Collectors.toSet());

        Set<CategoryEntity> unavailableCategories = categoryEntitiesAvailableAndUnavailable.stream()
                .skip(3)
                .collect(Collectors.toSet());

        List<CategoryEntity> unavailableCategorySaved = unavailableCategories.stream().toList();

        UserEntity user = new UserBuilder().withDefault().build();
        when(categoryCrudRepository.findAllByNameInAndUser(categoryEntitiesAvailableAndUnavailableNames, user))
                .thenReturn(availableCategories);
        when(categoryCrudRepository.saveAll(anySet())) // unavailable
                .thenReturn(unavailableCategorySaved);

        Set<CategoryEntity> actual = categoryGenerator.findOrCreateByName(categoryEntitiesAvailableAndUnavailableNames, user);

        verify(categoryCrudRepository).saveAll(unavailableCategoriesCaptor.capture());
        // yang mau di save harus 3, yang available harus 3 total 6
        assertEquals(3, unavailableCategoriesCaptor.getValue().size());
        assertEquals(actual.size(), categoryEntitiesAvailableAndUnavailableNames.size());
    }

    @Test
    @DisplayName("Should categoryEntity when given name and user")
    void generateCategoryByName_shouldReturnCategoryEntityWhenGivenNameAndUser() {
        CategoryEntity categoryEntities = new CategoryBuilder()
                .withDefault()
                .build();
        UserEntity user = new UserBuilder().withDefault().build();

        CategoryEntity actual = categoryGenerator.generateCategoryByName(categoryEntities.getName(), user);

        assertEquals(actual.getName(), categoryEntities.getName());
    }
}
