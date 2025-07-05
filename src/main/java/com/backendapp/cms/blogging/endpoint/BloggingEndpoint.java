package com.backendapp.cms.blogging.endpoint;

import com.backendapp.cms.openapi.blogging.api.BloggingControllerApi;
import com.backendapp.cms.openapi.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class BloggingEndpoint implements BloggingControllerApi {

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
    public ResponseEntity<PostArticle200Response> postArticle(PostRequest postRequest) {
        return BloggingControllerApi.super.postArticle(postRequest);
    }

    @Override
    public ResponseEntity<PostArticle200Response> updateSingleArticle(Long id, PostRequest postRequest) {
        return BloggingControllerApi.super.updateSingleArticle(id, postRequest);
    }
}
