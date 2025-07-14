package com.backendapp.cms.blogging.endpoint;

import com.backendapp.cms.blogging.converter.CategoryRequestConverter;
import com.backendapp.cms.blogging.converter.CategoryResponseConverter;
import com.backendapp.cms.blogging.converter.PostResponseConverter;
import com.backendapp.cms.blogging.dto.CategoryRequestDto;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.blogging.exception.NoCategoryException;
import com.backendapp.cms.blogging.exception.NoPostsException;
import com.backendapp.cms.blogging.repository.CategoryCrudRepository;
import com.backendapp.cms.blogging.repository.PostCrudRepository;
import com.backendapp.cms.blogging.service.CreateCategoryOperationPerformer;
import com.backendapp.cms.blogging.service.PostArticleOperationPerformer;
import com.backendapp.cms.openapi.blogging.api.BloggingControllerApi;
import com.backendapp.cms.openapi.dto.*;
import com.backendapp.cms.users.converter.UserResponseConverter;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// TODO get all categories
// TODO check all transaction in services

@Slf4j
@RestController
@AllArgsConstructor
public class BloggingEndpoint implements BloggingControllerApi {

    private final PostArticleOperationPerformer postArticleOperationPerformer;
    private final PostResponseConverter postResponseConverter;
    private final UserResponseConverter userResponseConverter;
    private final CategoryRequestConverter categoryRequestConverter;
    private final CategoryResponseConverter categoryResponseConverter;
    private final CreateCategoryOperationPerformer createCategoryOperationPerformer;
    private final PostCrudRepository postCrudRepository;
    private final CategoryCrudRepository categoryCrudRepository;

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostArticle200Response> postArticle(@Valid PostRequest postRequest) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostEntity article = postArticleOperationPerformer.post(user, postRequest);

        UserSimpleResponse userSimpleResponse = userResponseConverter.fromUserEntityToSimpleResponse(user);
        PostSimpleResponse postSimpleResponse = postResponseConverter.fromPostEntityToPostSimpleResponse(article);
        postSimpleResponse.setUser(userSimpleResponse);

        PostArticle200Response response = new PostArticle200Response();
        response.setSuccess(true);
        response.setMessage("Berhasil memposting sebuah artikel");
        response.setData(postSimpleResponse);

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateCategory200Response> createCategory(CategoryRequest categoryRequest) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CategoryRequestDto category = categoryRequestConverter.fromCategoryRequestToCategoryRequestDto(categoryRequest);
        CategoryEntity savedCategory = createCategoryOperationPerformer.createCategory(category, user);
        CategoriesSimpleDTO mappedCategory = categoryResponseConverter.fromCategoriesEntityToCategorySimpleDto(savedCategory);

        CreateCategory200Response response = new CreateCategory200Response();
        response.setSuccess(true);
        response.setMessage("Berhasil membuat kategori");
        response.setData(mappedCategory);

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetAllArticles200Response> getAllArticles() {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<PostSimpleResponse> presentPosts = postCrudRepository.findAllByUserAndDeletedAtIsNull(user)
                .orElseThrow(NoPostsException::new)
                .stream()
                .map(postResponseConverter::fromPostEntityToPostSimpleResponse)
                .toList();

        GetAllArticles200Response response = new GetAllArticles200Response();
        response.setSuccess(true);
        response.setMessage("Berhasil memuat data artikel");
        response.setData(presentPosts);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GetSingleArticle200Response> getSingleArticle(Long id) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostEntity post = postCrudRepository.findByIdAndUser(id, user).orElseThrow(NoPostsException::new);
        PostResponse postResponse = postResponseConverter.fromPostEntityToPostResponse(post);


        GetSingleArticle200Response response = new GetSingleArticle200Response();
        response.setSuccess(true);
        response.setMessage("Berhasil memuat data artikel");
        response.setData(postResponse);

        return ResponseEntity.ok(response);
    }

//    @Override
//    public ResponseEntity<GetSingleCategory200Response> getSingleCategory(Long id) {
//        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        CategoryEntity category = categoryCrudRepository.findByIdAndUser(id, user).orElseThrow(NoCategoryException::new);
//        CategoryResponse postResponse = categoryResponseConverter.fromCategoryEntityToCategoryResponse(category);
//
//
//        GetSingleArticle200Response response = new GetSingleArticle200Response();
//        response.setSuccess(true);
//        response.setMessage("Berhasil memuat data artikel");
//        response.setData(postResponse);
//
//        return ResponseEntity.ok(response);
//    }
}
