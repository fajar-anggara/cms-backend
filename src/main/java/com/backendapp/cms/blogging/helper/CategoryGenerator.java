package com.backendapp.cms.blogging.helper;

import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.exception.CategoryAlreadyExistsException;
import com.backendapp.cms.blogging.repository.CategoryCrudRepository;
import com.backendapp.cms.users.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class CategoryGenerator {

    private final CategoryCrudRepository categoryCrudRepository;
    private final CategorySanitizer categorySanitizer;

    /**
     * @param categoryNames Sudah di sanitasi di service, tidak akan null karena sudah di cek
     * @param user UserEntity dari securityContextHolder
     * @return
     */
    public Set<CategoryEntity> findOrCreateByName(List<String> categoryNames, UserEntity user) {
        Set<CategoryEntity> availableCategories = categoryCrudRepository.findAllByNameInAndUser(categoryNames, user);
        log.info("category that get by name and user {}", availableCategories);
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

    public CategoryEntity checkOrCreate(String categoryNames, UserEntity user) {
        String categoryName = categorySanitizer.toPlainText(categoryNames);
        Optional<CategoryEntity> category = categoryCrudRepository.findByNameAndUser(categoryName, user);
        if (category.isEmpty()) {
            return generateCategoryByName(categoryNames, user);
        }

        throw new CategoryAlreadyExistsException();
    }

    public CategoryEntity generateCategoryByName(String name, UserEntity user) {
        return CategoryEntity
                .builder()
                .name(name)
                .user(user)
                .slug(categorySanitizer.toSlugFormat(name))
                .build();
    }
}