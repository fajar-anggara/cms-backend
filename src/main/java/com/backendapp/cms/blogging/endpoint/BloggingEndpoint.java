package com.backendapp.cms.blogging.endpoint;

import com.backendapp.cms.blogging.converter.CategoryRequestConverter;
import com.backendapp.cms.blogging.converter.CategoryResponseConverter;
import com.backendapp.cms.blogging.converter.PostRequestConverter;
import com.backendapp.cms.blogging.converter.PostResponseConverter;
import com.backendapp.cms.blogging.dto.CategoryRequestDto;
import com.backendapp.cms.blogging.dto.PostRequestDto;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.blogging.exception.NoCategoryException;
import com.backendapp.cms.blogging.exception.NoPostsException;
import com.backendapp.cms.blogging.repository.CategoryCrudRepository;
import com.backendapp.cms.blogging.repository.PostCrudRepository;
import com.backendapp.cms.blogging.service.*;
import com.backendapp.cms.common.enums.Deleted;
import com.backendapp.cms.common.enums.SortBy;
import com.backendapp.cms.common.enums.SortOrder;
import com.backendapp.cms.openapi.blogging.api.BloggingControllerApi;
import com.backendapp.cms.openapi.dto.*;
import com.backendapp.cms.superuser.converter.PaginationConverter;
import com.backendapp.cms.users.converter.UserResponseConverter;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private final GetAllArticleOperationPerformer getAllArticleOperationPerformer;
    private final PaginationConverter paginationConverter;
    private final PostRequestConverter postRequestConverter;
    private final UpdateSingleCategoryOperationPerformer updateSingleCategoryOperationPerformer;
    private final UpdateSingleArticleOperationPerformer updateSingleArticleOperationPerformer;

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
    public ResponseEntity<GetAllArticles200Response> getAllArticles(
            String deleted,
            Integer limit,
            String sortBy,
            String sortOrder,
            String categorySlug,
            Integer page,
            String search) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<PostEntity> articles = getAllArticleOperationPerformer.getAllByUser(
                Deleted.valueOf(deleted),
                limit,
                SortBy.valueOf(sortBy).value,
                SortOrder.valueOf(sortOrder).direction,
                categorySlug,
                page,
                search,
                user
        );
        List<PostSimpleResponse> mappedArticles = articles
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

        GetAllArticles200ResponseAllOfData data = new GetAllArticles200ResponseAllOfData();
        data.setPosts(mappedArticles);
        data.setPagination(pagination);

        GetAllArticles200Response response = new GetAllArticles200Response();
        response.setSuccess(true);
        response.setMessage("Berhasil memuat semua artikel");
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetAllCategories200Response> getAllCategories() {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<GetAllCategories200ResponseAllOfData> listOfCategory = categoryCrudRepository.findAllByUserAndDeletedAtIsNull(user)
                .orElse(new ArrayList<>())
                .stream()
                .map(categoryResponseConverter::fromCategoryEntityToGetAllCategories200ResponseAllOfData)
                .toList();

        GetAllCategories200Response response = new GetAllCategories200Response();
        response.setSuccess(true);
        response.setMessage("Berhasil memuat data kategori");
        response.setData(listOfCategory);

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetSingleArticle200Response> getSingleArticle(Long id) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostEntity post = postCrudRepository.findByIdAndUserAndDeletedAtIsNull(id, user).orElseThrow(NoPostsException::new);
        PostResponse postResponse = postResponseConverter.fromPostEntityToPostResponse(post);


        GetSingleArticle200Response response = new GetSingleArticle200Response();
        response.setSuccess(true);
        response.setMessage("Berhasil memuat data artikel");
        response.setData(postResponse);

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetSingleCategory200Response> getSingleCategory(
            Long id,
            String deleted,
            Integer limit,
            String sortBy,
            String sortOrder,
            Integer page,
            String search) {
        if (id == null) throw new NoCategoryException();
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CategoryEntity category = getAllArticleOperationPerformer.getCategoryById(id, user);

        Page<PostEntity> articles = getAllArticleOperationPerformer.getAllByCategory(
                category,
                Deleted.valueOf(deleted),
                limit,
                SortBy.valueOf(sortBy).value,
                SortOrder.valueOf(sortOrder).direction,
                page, // Page ganti menjadi size, dan buat page yang benar.
                search,
                user
        );
        List<PostSimpleDTO> mappedArticles = articles
                .stream()
                .map(postResponseConverter::fromPostEntityToPostSimpleDTO)
                .toList();
        PaginationMetadata pagination = paginationConverter.toPaginationMetadata(
                articles.getNumber(),
                articles.getSize(),
                articles.getTotalElements(),
                articles.getTotalPages(),
                articles.hasNext(),
                articles.hasPrevious()
        );

        GetSingleCategory200ResponseAllOfData data = new GetSingleCategory200ResponseAllOfData();
        data.setCategory(categoryResponseConverter.fromCategoryEntityToCategoryResponse(category));
        data.setPosts(mappedArticles);
        data.setPagination(pagination);

        GetSingleCategory200Response response = new GetSingleCategory200Response();
        response.setSuccess(true);
        response.setMessage("Berhasil memuat semua data kategori");
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EditSingleCategory200Response> editSingleCategory(Long id, CategoryRequest categoryRequest) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CategoryRequestDto category = categoryRequestConverter.fromCategoryRequestToCategoryRequestDto(categoryRequest);

        CategoryEntity updateCategory = updateSingleCategoryOperationPerformer.update(id, category, user);
        CategoriesSimpleDTO data = categoryResponseConverter.fromCategoriesEntityToCategorySimpleDto(updateCategory);

        EditSingleCategory200Response response = new EditSingleCategory200Response();
        response.setSuccess(true);
        response.setMessage("Berhasil mengedit kategori");
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostArticle200Response> updateSingleArticle(Long id, PostRequest postRequest) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostRequestDto article = postRequestConverter.fromPostRequestToPostRequestDto(postRequest);

        PostEntity updatedArticle = updateSingleArticleOperationPerformer.update(id, article, user);
        PostSimpleResponse data = postResponseConverter.fromPostEntityToPostSimpleResponse(updatedArticle);

        PostArticle200Response response = new PostArticle200Response();
        response.setSuccess(true);
        response.setMessage("Berhasil mengedit data artikel");
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Success200Response> deleteSingleCategory(Long id) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CategoryEntity category = categoryCrudRepository.findByIdAndUser(id, user).orElseThrow(NoCategoryException::new);
        category.setDeletedAt(LocalDateTime.now());
        categoryCrudRepository.save(category);

        return ResponseEntity.ok(new Success200Response(true, "Berhasil menghapus kategori"));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Success200Response> deleteSingleArticle(Long id) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostEntity post = postCrudRepository.findByIdAndUser(id, user).orElseThrow(NoPostsException::new);
        post.setDeletedAt(LocalDateTime.now());
        postCrudRepository.save(post);

        return ResponseEntity.ok(new Success200Response(true, "Berhasil menghapus artikel"));
    }
}
