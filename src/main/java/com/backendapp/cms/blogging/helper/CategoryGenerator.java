package com.backendapp.cms.blogging.helper;

import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.repository.CategoryCrudRepository;
import com.backendapp.cms.users.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CategoryGenerator {

    private final CategoryCrudRepository categoryCrudRepository;
    private final CategorySanitizer categorySanitizer;

    public Set<CategoryEntity> findOrCreateByName(List<String> categoryNames, UserEntity user) {
        Set<CategoryEntity> availableCategories = categoryCrudRepository.findAllByNameInAndUser(categoryNames, user);
        Set<String> nameOfAvailableCategories = availableCategories
                .stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toSet());

        // Unavailable categories
        Set<CategoryEntity> unAvailableCategories = categoryNames
                .stream()
                .filter(nameOfUnAvailableCategories -> !nameOfAvailableCategories.contains(nameOfUnAvailableCategories))
                .map(categoryName -> generateCategoryByName(categoryName, user))
                .collect(Collectors.toSet());

        List<CategoryEntity> createdCategories = categoryCrudRepository.saveAll(unAvailableCategories);

        availableCategories.addAll(createdCategories);
        return availableCategories;
    }

    public CategoryEntity generateCategoryByName(String name, UserEntity user) {
        return CategoryEntity
                .builder()
                .name(name)
                .user(user)
                .slug(categorySanitizer.sanitizeSlug(name))
                .build();
    }
}
