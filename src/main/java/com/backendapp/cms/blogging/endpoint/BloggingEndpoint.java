package com.backendapp.cms.blogging.endpoint;

import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.blogging.helper.PostGetCategories;
import com.backendapp.cms.blogging.service.PostArticleOperationPerformer;
import com.backendapp.cms.openapi.blogging.api.BloggingControllerApi;
import com.backendapp.cms.openapi.dto.*;
import com.backendapp.cms.users.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class BloggingEndpoint implements BloggingControllerApi {

    private final PostArticleOperationPerformer postArticleOperationPerformer;
    private final PostGetCategories postGetCategories;

    @Override
    public ResponseEntity<BlogFullResponse> adminDashboard(Integer limit, String sortBy, String sortOrder, String categorySlug, String search) {
        return BloggingControllerApi.super.adminDashboard(limit, sortBy, sortOrder, categorySlug, search);
    }

    @Override
    public ResponseEntity<List<List>> categoriesDashboard() {
        return BloggingControllerApi.super.categoriesDashboard();
    }

    @Override
    public ResponseEntity<CategoriesSimpleDTO> createCategory(CategoryRequest categoryRequest) {
        return BloggingControllerApi.super.createCategory(categoryRequest);
    }

    @Override
    public ResponseEntity<Success200Response> deleteSingleArticle(Long id) {
        return BloggingControllerApi.super.deleteSingleArticle(id);
    }

    @Override
    public ResponseEntity<Success200Response> deleteSingleCategory(Long id) {
        return BloggingControllerApi.super.deleteSingleCategory(id);
    }

    @Override
    public ResponseEntity<GetSingleArticle200Response> editSingleArticleCategories(Long id, List<PostRequest> postRequest) {
        return BloggingControllerApi.super.editSingleArticleCategories(id, postRequest);
    }

    @Override
    public ResponseEntity<EditSingleCategory200Response> editSingleCategory(Long id, CategoryRequest categoryRequest) {
        return BloggingControllerApi.super.editSingleCategory(id, categoryRequest);
    }

    @Override
    public ResponseEntity<GetSingleCategory200Response> editSingleCategoryArticles(Long id, List<CategoryRequest> categoryRequest) {
        return BloggingControllerApi.super.editSingleCategoryArticles(id, categoryRequest);
    }

    @Override
    public ResponseEntity<GetAllArticles200Response> getAllArticles() {
        return BloggingControllerApi.super.getAllArticles();
    }

    @Override
    public ResponseEntity<GetSingleArticle200Response> getSingleArticle(Long id) {
        return BloggingControllerApi.super.getSingleArticle(id);
    }

    @Override
    public ResponseEntity<GetSingleCategory200Response> getSingleCategory(Long id) {
        return BloggingControllerApi.super.getSingleCategory(id);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostArticle200Response> postArticle(PostRequest postRequest) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostEntity article = postArticleOperationPerformer.post(user, postRequest);
        Optional<List<Long>> categoryIds = postRequest.getCategories().isPresent() ? Optional.ofNullable(postRequest.getCategories().get()) : Optional.empty();
        HashSet<CategoryEntity> categories = postGetCategories.byId(categoryIds, user);




    }

    @Override
    public ResponseEntity<PostArticle200Response> updateSingleArticle(Long id, PostRequest postRequest) {
        return BloggingControllerApi.super.updateSingleArticle(id, postRequest);
    }
}
