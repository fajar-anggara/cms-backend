package com.backendapp.cms.blogging.service;

import com.backendapp.cms.blogging.dto.CategoryRequestDto;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.blogging.exception.NoCategoryException;
import com.backendapp.cms.blogging.helper.CategoryGenerator;
import com.backendapp.cms.blogging.helper.CategorySanitizer;
import com.backendapp.cms.blogging.repository.CategoryCrudRepository;
import com.backendapp.cms.blogging.repository.PostCrudRepository;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UpdateSingleCategoryOperationPerformer {

    private final CategoryCrudRepository categoryCrudRepository;
    private final CategorySanitizer categorySanitizer;
    private final CategoryGenerator categoryGenerator;
    private final PostCrudRepository postCrudRepository;

    @Transactional
    public CategoryEntity update(Long categoryId, CategoryRequestDto category, UserEntity user) {
        CategoryEntity categoryEntity = categoryCrudRepository.findByIdAndUserAndDeletedAtIsNull(categoryId, user).orElseThrow(NoCategoryException::new);
        CategoryEntity duplicationCheck = categoryGenerator.checkOrCreate(categorySanitizer.toPlainText(category.getName()), user);


        categoryEntity.setName(duplicationCheck.getName());
        categoryEntity.setSlug(duplicationCheck.getSlug());
        category.getDescription().ifPresent(description -> categoryEntity.setDescription(categorySanitizer.toPlainText(description)));
        category.getPosts().ifPresent(post -> {
            Set<PostEntity> posts = postCrudRepository.findAllByIdInAndUser(post, user);
            HashSet<PostEntity> finalPosts = new HashSet<>(posts);
            finalPosts.addAll(posts);
            finalPosts.addAll(categoryEntity.getPosts());
            categoryEntity.setPosts(finalPosts);
        });

        return categoryCrudRepository.save(categoryEntity);
    }
}
