package com.backendapp.cms.blogging.service;

import com.backendapp.cms.blogging.converter.PostRequestConverter;
import com.backendapp.cms.blogging.dto.PostRequestDto;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.blogging.helper.CategoryGenerator;
import com.backendapp.cms.blogging.helper.CategorySanitizer;
import com.backendapp.cms.blogging.helper.PostGenerator;
import com.backendapp.cms.blogging.helper.PostSanitizer;
import com.backendapp.cms.blogging.repository.PostCrudRepository;
import com.backendapp.cms.common.enums.Status;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.openapi.dto.PostRequest;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class PostArticleOperationPerformer {

    private final PostCrudRepository postCrudRepository;
    private final CategoryGenerator categoryGenerator;
    private final PostRequestConverter postRequestConverter;
    private final PostGenerator postGenerator;
    private final PostSanitizer postSanitizer;
    private final CategorySanitizer categorySanitizer;
    private final Validator validator;


    @Transactional
    public PostEntity post(UserEntity user, PostRequest request) {
        PostRequestDto mappedRequest = postRequestConverter.fromPostRequestToPostRequestDto(request);
        PostRequestDto sanitizedRequest = postSanitizer.sanitize(mappedRequest);

        // validate
        Set<ConstraintViolation<PostRequestDto>> violations = validator.validate(sanitizedRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        // prepare categories
        Set<CategoryEntity> categories = new HashSet<>();
        if (request.getCategories() != null || !request.getCategories().isEmpty()) {
            List<String> sanitizedCategories = request.getCategories()
                    .stream()
                    .map(CategoriesSimpleDTO::getName)
                    .map(categorySanitizer::toPlainText)
                    .toList();

            categories = categoryGenerator.findOrCreateByName(sanitizedCategories, user);
        }

        PostEntity article = PostEntity.builder()
                .title(sanitizedRequest.getTitle())
                .slug(postGenerator.generateSlug(sanitizedRequest.getTitle()))
                .content(sanitizedRequest.getContent())
                .excerpt(sanitizedRequest.getExcerpt()
                        .orElseGet(() -> {
                            return postGenerator.generateExcerpt(sanitizedRequest.getContent());
                        }))
                .featuredImageUrl(sanitizedRequest.getFeaturedImageUrl().orElseGet(() -> {
                    return null;
                }))
                .status(sanitizedRequest.getStatus()
                        .orElseGet(() -> {
                            return Status.PUBLISHED;
                        }))
                .user(user)
                .categories(categories)
                .publishedAt(postGenerator.getPublishedAt())
                .createdAt(LocalDateTime.now())
                .build();

        return postCrudRepository.save(article);
    }
}
