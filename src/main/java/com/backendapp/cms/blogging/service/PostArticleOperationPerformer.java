package com.backendapp.cms.blogging.service;

import com.backendapp.cms.blogging.converter.PostRequestConverter;
import com.backendapp.cms.blogging.dto.PostRequestDto;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.blogging.helper.PostGenerator;
import com.backendapp.cms.blogging.helper.PostGetCategories;
import com.backendapp.cms.blogging.helper.PostSanitizer;
import com.backendapp.cms.blogging.repository.CategoryCrudRepository;
import com.backendapp.cms.blogging.repository.PostCrudRepository;
import com.backendapp.cms.common.enums.Status;
import com.backendapp.cms.openapi.dto.PostRequest;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PostArticleOperationPerformer {

    private final PostCrudRepository postCrudRepository;
    private final PostGetCategories postGetCategories;
    private final PostRequestConverter postRequestConverter;
    private final PostGenerator postGenerator;
    private final PostSanitizer postSanitizer;

    @Transactional
    public PostEntity post(UserEntity user, PostRequest request) {
        PostRequestDto mappedRequest = postRequestConverter.fromPostRequestToPostRequestDto(request);
        PostRequestDto sanitizedRequest = postSanitizer.sanitize(mappedRequest);

        PostEntity article = PostEntity.builder()
                .title(sanitizedRequest.getTitle())
                .slug(sanitizedRequest.getSlug()
                        .orElseGet(() -> {
                            return postGenerator.generateSlug(sanitizedRequest.getTitle());
                        }))
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
                .categories(postGetCategories.byId(sanitizedRequest.getCategories(), user))
                .publishedAt(postGenerator.getPublishedAt())
                .createdAt(LocalDateTime.now())
                .build();
        log.info(article.getContent());
        return postCrudRepository.save(article);
    }
}
