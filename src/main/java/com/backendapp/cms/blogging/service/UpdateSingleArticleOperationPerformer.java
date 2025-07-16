package com.backendapp.cms.blogging.service;

import com.backendapp.cms.blogging.dto.PostRequestDto;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.blogging.exception.NoPostsException;
import com.backendapp.cms.blogging.helper.CategoryGenerator;
import com.backendapp.cms.blogging.helper.CategorySanitizer;
import com.backendapp.cms.blogging.helper.PostSanitizer;
import com.backendapp.cms.blogging.repository.PostCrudRepository;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UpdateSingleArticleOperationPerformer {

    private final PostCrudRepository postCrudRepository;
    private final PostSanitizer postSanitizer;
    private final Validator validator;
    private final CategorySanitizer categorySanitizer;
    private final CategoryGenerator categoryGenerator;

    @Transactional
    public PostEntity update(Long id, PostRequestDto article, UserEntity user) {
        PostEntity post = postCrudRepository.findByIdAndUser(id, user).orElseThrow(NoPostsException::new);
        PostRequestDto sanitizedArticle = postSanitizer.sanitize(article);

        // validate
        Set<ConstraintViolation<PostRequestDto>> violations = validator.validate(sanitizedArticle);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        // prepare categories
        Set<CategoryEntity> categories;
        if (article.getCategories().isPresent()) {
            List<String> sanitizedCategories = article.getCategories().get()
                    .stream()
                    .map(CategoriesSimpleDTO::getName)
                    .map(categorySanitizer::toPlainText)
                    .toList();

            categories = categoryGenerator.findOrCreateByName(sanitizedCategories, user);
        } else {
            categories = new HashSet<>();
        }

        post.setTitle(sanitizedArticle.getTitle());
        sanitizedArticle.getSlug().ifPresent(post::setSlug);
        post.setContent(sanitizedArticle.getContent());
        sanitizedArticle.getExcerpt().ifPresent(post::setExcerpt);
        sanitizedArticle.getFeaturedImageUrl().ifPresent(post::setFeaturedImageUrl);
        sanitizedArticle.getStatus().ifPresent(post::setStatus);
        sanitizedArticle.getCategories().ifPresent((newCategories) -> post.setCategories(categories));
        post.setUpdatedAt(LocalDateTime.now());

        return postCrudRepository.save(post);
    }
}
