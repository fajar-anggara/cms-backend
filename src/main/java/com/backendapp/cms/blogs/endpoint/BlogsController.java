package com.backendapp.cms.blogs.endpoint;

import com.backendapp.cms.blogging.converter.CategoryResponseConverter;
import com.backendapp.cms.blogging.converter.PostResponseConverter;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.blogging.exception.NoCategoryException;
import com.backendapp.cms.blogging.exception.NoPostsException;
import com.backendapp.cms.blogging.repository.CategoryCrudRepository;
import com.backendapp.cms.blogging.repository.PostCrudRepository;
import com.backendapp.cms.blogs.exception.UserNotFoundException;
import com.backendapp.cms.blogs.service.GetArticlesOperationPerformer;
import com.backendapp.cms.common.enums.SortBy;
import com.backendapp.cms.common.enums.SortOrder;
import com.backendapp.cms.openapi.blogs.api.BlogControllerApi;
import com.backendapp.cms.openapi.dto.*;
import com.backendapp.cms.superuser.converter.PaginationConverter;
import com.backendapp.cms.users.converter.UserResponseConverter;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.repository.UserCrudRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BlogsController implements BlogControllerApi {

    private final PostCrudRepository postCrudRepository;
    private final UserCrudRepository userCrudRepository;
    private final GetArticlesOperationPerformer getArticlesOperationPerformer;
    private final UserResponseConverter userResponseConverter;
    private final PostResponseConverter postResponseConverter;
    private final CategoryCrudRepository categoryCrudRepository;
    private final CategoryResponseConverter categoryResponseConverter;
    private final PaginationConverter paginationConverter;


    @Override
    public ResponseEntity<BlogSingleResponse> singlePost(String username, Long postId) {
        UserEntity user = userCrudRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        PostEntity post = postCrudRepository.findByIdAndUser(postId, user).orElseThrow(NoPostsException::new);
        List<CategoryEntity> category = categoryCrudRepository.findAllByUserAndDeletedAtIsNull(user).orElseThrow(NoCategoryException::new);

        UserSimpleResponse userSimpleResponse = userResponseConverter.fromUserEntityToSimpleResponse(user);
        PostResponse postResponse = postResponseConverter.fromPostEntityToPostResponse(post);
        List<CategoriesSimpleDTO> categorySimpleResponses = category
                .stream()
                .map(categoryResponseConverter::fromCategoriesEntityToCategorySimpleDto)
                .toList();

        BlogSingleResponse blogSingleResponse = new BlogSingleResponse();
        blogSingleResponse.setUser(userSimpleResponse);
        blogSingleResponse.setArticles(postResponse);
        blogSingleResponse.setCategories(categorySimpleResponses);

        return ResponseEntity.ok(blogSingleResponse);
    }

    @Override
    public ResponseEntity<BlogFullResponse> userLandingPage(
            String userName,
            Integer page,
            Integer limit,
            String sortBy,
            String sortOrder,
            String categorySlug,
            String search) {
        UserEntity user = userCrudRepository.findByUsername(userName).orElseThrow(UserNotFoundException::new);
        List<CategoryEntity> category = categoryCrudRepository.findAllByUserAndDeletedAtIsNull(user).orElseThrow(NoCategoryException::new);
        Page<PostEntity> articles = getArticlesOperationPerformer.getAllPosts(
                user,
                limit,
                page,
                SortBy.valueOf(sortBy).value,
                SortOrder.valueOf(sortOrder).direction,
                categorySlug,
                search
        );

        UserSimpleResponse userSimpleResponse = userResponseConverter.fromUserEntityToSimpleResponse(user);
        List<CategoriesSimpleDTO> categorySimpleResponses = category
                .stream()
                .map(categoryResponseConverter::fromCategoriesEntityToCategorySimpleDto)
                .toList();
        List<PostSimpleResponse> posts = articles
                .stream()
                .map(postResponseConverter::fromPostEntityToPostSimpleResponse)
                .toList();
        PaginationMetadata pagination = paginationConverter.toPaginationMetadata(
                articles.getNumber(),
                articles.getSize(),
                articles.getTotalElements(),
                articles.getTotalPages(),
                articles.hasNext(),
                articles.hasPrevious()
        );

        BlogFullResponsePosts responsePosts = new BlogFullResponsePosts();
        responsePosts.setArticles(posts);
        responsePosts.setPagination(pagination);

        BlogFullResponse blogFullResponse = new BlogFullResponse();
        blogFullResponse.setUser(userSimpleResponse);
        blogFullResponse.setPosts(responsePosts);
        blogFullResponse.setCategories(categorySimpleResponses);

        return ResponseEntity.ok(blogFullResponse);
    }
}
