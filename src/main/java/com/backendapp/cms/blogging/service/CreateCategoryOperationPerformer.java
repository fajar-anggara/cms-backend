package com.backendapp.cms.blogging.service;

import com.backendapp.cms.blogging.dto.CategoryRequestDto;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.helper.CategoryGenerator;
import com.backendapp.cms.blogging.helper.CategorySanitizer;
import com.backendapp.cms.blogging.repository.CategoryCrudRepository;
import com.backendapp.cms.blogging.repository.PostCrudRepository;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
public class CreateCategoryOperationPerformer {

    private final CategoryCrudRepository categoryCrudRepository;
    private final PostCrudRepository postCrudRepository;
    private final CategoryGenerator categoryGenerator;
    private final CategorySanitizer categorySanitizer;

    @Transactional
    public CategoryEntity createCategory(@Valid CategoryRequestDto category, UserEntity user) {
        // sanitized
        String categoryName = categorySanitizer.toPlainText(category.getName());

        CategoryEntity categoryEntity = categoryGenerator.checkOrCreate(categoryName, user);
        category.getDescription().ifPresent(categoryEntity::setDescription);
        category.getPosts().ifPresent(postCrudRepository::findAllById);

        return categoryCrudRepository.save(categoryEntity);
    }
}
