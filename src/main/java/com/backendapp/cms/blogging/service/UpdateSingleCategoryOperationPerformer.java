package com.backendapp.cms.blogging.service;

import com.backendapp.cms.blogging.dto.CategoryRequestDto;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.blogging.exception.CategoryAlreadyExistsException;
import com.backendapp.cms.blogging.exception.NoCategoryException;
import com.backendapp.cms.blogging.helper.CategoryGenerator;
import com.backendapp.cms.blogging.helper.CategorySanitizer;
import com.backendapp.cms.blogging.repository.CategoryCrudRepository;
import com.backendapp.cms.blogging.repository.PostCrudRepository;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class UpdateSingleCategoryOperationPerformer {

    private final CategoryCrudRepository categoryCrudRepository;
    private final CategorySanitizer categorySanitizer;
    private final CategoryGenerator categoryGenerator;
    private final PostCrudRepository postCrudRepository;

    @Transactional
    public CategoryEntity update(Long categoryId, CategoryRequestDto category, UserEntity user) {
        CategoryEntity categoryEntity = categoryCrudRepository.findByIdAndUserAndDeletedAtIsNull(categoryId, user)
                .orElseThrow(NoCategoryException::new);

        if (!category.getName().equals(categoryEntity.getName())) {
            CategoryEntity generatedCategory = categoryGenerator.generateCategoryByName(
                    categorySanitizer.toPlainText(category.getName()), user);

            if (categoryCrudRepository.existsByNameAndUser(generatedCategory.getName(), user)) {
                throw new CategoryAlreadyExistsException();
            }

            categoryEntity.setName(generatedCategory.getName());
            categoryEntity.setSlug(generatedCategory.getSlug());
        }

        category.getDescription().ifPresent(description ->
                categoryEntity.setDescription(categorySanitizer.toPlainText(description)));

        Set<PostEntity> oldPosts = categoryEntity.getPosts();
        if (oldPosts != null && !oldPosts.isEmpty()) {
            oldPosts.forEach(post -> {
                post.getCategories().remove(categoryEntity);
            });
            postCrudRepository.saveAll(oldPosts);
        }

        Set<PostEntity> newPosts = new HashSet<>();
        if (category.getPosts().isPresent() && !category.getPosts().get().isEmpty()) {
            newPosts = postCrudRepository.findAllByIdInAndUser(category.getPosts().get(), user);
            newPosts.forEach(post -> {
                post.getCategories().add(categoryEntity);
            });
            postCrudRepository.saveAll(newPosts);
        }

        categoryEntity.setPosts(newPosts);
        CategoryEntity savedCategory = categoryCrudRepository.save(categoryEntity);

        return savedCategory;
    }
}
