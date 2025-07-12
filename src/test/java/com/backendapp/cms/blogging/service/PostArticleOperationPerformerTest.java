package com.backendapp.cms.blogging.service;

import com.backendapp.cms.blogging.contract.bonded.AuthenticatedUserDummy;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostArticleOperationPerformerTest {

    @Mock
    private PostCrudRepository postCrudRepository;

    @Mock
    private CategoryGenerator categoryGenerator;

    @Mock
    private PostRequestConverter postRequestConverter;

    @Mock
    private PostGenerator postGenerator;

    @Mock
    private PostSanitizer postSanitizer;

    @Mock
    private CategorySanitizer categorySanitizer;

    @InjectMocks
    private PostArticleOperationPerformer postArticleOperationPerformer;

    private UserEntity user = AuthenticatedUserDummy.BLOGGER_USER;
    private PostRequest postRequest = PostSanitizerContract.UNCONVERTED_UNSANITIZED_RAWREQUEST;
    private PostRequestDto postRequestDto = PostSanitizerContract.CONVERTED_UNSANITIZED_REQUEST;
    private PostRequestDto sanitizedPostRequestDto = PostSanitizerContract.CONVERTED_SANITIZED_REQUEST;
    private PostEntity savedPostEntity = ;
    private Set<CategoryEntity> mockCategories;
    private List<CategoriesSimpleDTO> mockCategoriesDTO;

    @BeforeEach
    void setUp() {
        // Setup mock user
        mockUser = UserEntity.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        // Setup mock categories DTO
        CategoriesSimpleDTO category1 = new CategoriesSimpleDTO();
        category1.setName("Technology");
        CategoriesSimpleDTO category2 = new CategoriesSimpleDTO();
        category2.setName("Java");
        mockCategoriesDTO = Arrays.asList(category1, category2);

        // Setup mock post request
        mockPostRequest = PostRequest.builder()
                .title("Test Post Title")
                .slug("test-post-slug")
                .content("This is test content")
                .excerpt("Test excerpt")
                .featuredImageUrl("https://example.com/image.jpg")
                .status(Status.PUBLISHED)
                .categories(mockCategoriesDTO)
                .build();

        // Setup mock post request DTO
        mockPostRequestDto = PostRequestDto.builder()
                .title("Test Post Title")
                .slug("test-post-slug")
                .content("This is test content")
                .excerpt(Optional.of("Test excerpt"))
                .featuredImageUrl(Optional.of("https://example.com/image.jpg"))
                .status(Optional.of(Status.PUBLISHED))
                .build();

        // Setup sanitized post request DTO
        sanitizedPostRequestDto = PostRequestDto.builder()
                .title("Sanitized Test Post Title")
                .slug("sanitized-test-post-slug")
                .content("This is sanitized test content")
                .excerpt(Optional.of("Sanitized test excerpt"))
                .featuredImageUrl(Optional.of("https://example.com/image.jpg"))
                .status(Optional.of(Status.PUBLISHED))
                .build();

        // Setup mock categories
        CategoryEntity category1Entity = CategoryEntity.builder()
                .id(1L)
                .name("Technology")
                .slug("technology")
                .build();
        CategoryEntity category2Entity = CategoryEntity.builder()
                .id(2L)
                .name("Java")
                .slug("java")
                .build();
        mockCategories = Set.of(category1Entity, category2Entity);

        // Setup mock saved post
        mockSavedPost = PostEntity.builder()
                .id(1L)
                .title("Sanitized Test Post Title")
                .slug("generated-slug")
                .content("This is sanitized test content")
                .excerpt("Sanitized test excerpt")
                .featuredImageUrl("https://example.com/image.jpg")
                .status(Status.PUBLISHED)
                .user(mockUser)
                .categories(mockCategories)
                .publishedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void post_ShouldCreatePostSuccessfully_WhenValidInputProvided() {
        // Arrange
        when(postRequestConverter.fromPostRequestToPostRequestDto(mockPostRequest))
                .thenReturn(mockPostRequestDto);
        when(postSanitizer.sanitize(mockPostRequestDto))
                .thenReturn(sanitizedPostRequestDto);
        when(categorySanitizer.toPlainText("Technology"))
                .thenReturn("technology");
        when(categorySanitizer.toPlainText("Java"))
                .thenReturn("java");
        when(categoryGenerator.findOrCreateByName(anyList(), eq(mockUser)))
                .thenReturn(mockCategories);
        when(postGenerator.generateSlug(anyString(), anyString()))
                .thenReturn("generated-slug");
        when(postGenerator.generateExcerpt(anyString()))
                .thenReturn("generated-excerpt");
        when(postGenerator.getPublishedAt())
                .thenReturn(LocalDateTime.now());
        when(postCrudRepository.save(any(PostEntity.class)))
                .thenReturn(mockSavedPost);

        // Act
        PostEntity result = postArticleOperationPerformer.post(mockUser, mockPostRequest);

        // Assert
        assertNotNull(result);
        assertEquals(mockSavedPost.getId(), result.getId());
        assertEquals(mockSavedPost.getTitle(), result.getTitle());
        assertEquals(mockSavedPost.getSlug(), result.getSlug());
        assertEquals(mockSavedPost.getContent(), result.getContent());

        // Verify all dependencies were called
        verify(postRequestConverter).fromPostRequestToPostRequestDto(mockPostRequest);
        verify(postSanitizer).sanitize(mockPostRequestDto);
        verify(categorySanitizer, times(2)).toPlainText(anyString());
        verify(categoryGenerator).findOrCreateByName(anyList(), eq(mockUser));
        verify(postGenerator).generateSlug(anyString(), anyString());
        verify(postGenerator).getPublishedAt();
        verify(postCrudRepository).save(any(PostEntity.class));
    }

    @Test
    void post_ShouldUseGeneratedExcerpt_WhenExcerptIsEmpty() {
        // Arrange
        PostRequestDto dtoWithoutExcerpt = PostRequestDto.builder()
                .title("Test Title")
                .slug("test-slug")
                .content("Test content")
                .excerpt(Optional.empty())
                .featuredImageUrl(Optional.of("https://example.com/image.jpg"))
                .status(Optional.of(Status.PUBLISHED))
                .build();

        when(postRequestConverter.fromPostRequestToPostRequestDto(mockPostRequest))
                .thenReturn(mockPostRequestDto);
        when(postSanitizer.sanitize(mockPostRequestDto))
                .thenReturn(dtoWithoutExcerpt);
        when(categorySanitizer.toPlainText(anyString()))
                .thenReturn("sanitized-category");
        when(categoryGenerator.findOrCreateByName(anyList(), eq(mockUser)))
                .thenReturn(mockCategories);
        when(postGenerator.generateSlug(anyString(), anyString()))
                .thenReturn("generated-slug");
        when(postGenerator.generateExcerpt("Test content"))
                .thenReturn("generated-excerpt");
        when(postGenerator.getPublishedAt())
                .thenReturn(LocalDateTime.now());
        when(postCrudRepository.save(any(PostEntity.class)))
                .thenReturn(mockSavedPost);

        // Act
        PostEntity result = postArticleOperationPerformer.post(mockUser, mockPostRequest);

        // Assert
        assertNotNull(result);
        verify(postGenerator).generateExcerpt("Test content");

        // Capture the PostEntity passed to save method
        ArgumentCaptor<PostEntity> postCaptor = ArgumentCaptor.forClass(PostEntity.class);
        verify(postCrudRepository).save(postCaptor.capture());
        PostEntity capturedPost = postCaptor.getValue();
        assertEquals("generated-excerpt", capturedPost.getExcerpt());
    }

    @Test
    void post_ShouldUseDefaultStatus_WhenStatusIsEmpty() {
        // Arrange
        PostRequestDto dtoWithoutStatus = PostRequestDto.builder()
                .title("Test Title")
                .slug("test-slug")
                .content("Test content")
                .excerpt(Optional.of("Test excerpt"))
                .featuredImageUrl(Optional.of("https://example.com/image.jpg"))
                .status(Optional.empty())
                .build();

        when(postRequestConverter.fromPostRequestToPostRequestDto(mockPostRequest))
                .thenReturn(mockPostRequestDto);
        when(postSanitizer.sanitize(mockPostRequestDto))
                .thenReturn(dtoWithoutStatus);
        when(categorySanitizer.toPlainText(anyString()))
                .thenReturn("sanitized-category");
        when(categoryGenerator.findOrCreateByName(anyList(), eq(mockUser)))
                .thenReturn(mockCategories);
        when(postGenerator.generateSlug(anyString(), anyString()))
                .thenReturn("generated-slug");
        when(postGenerator.getPublishedAt())
                .thenReturn(LocalDateTime.now());
        when(postCrudRepository.save(any(PostEntity.class)))
                .thenReturn(mockSavedPost);

        // Act
        PostEntity result = postArticleOperationPerformer.post(mockUser, mockPostRequest);

        // Assert
        assertNotNull(result);

        // Capture the PostEntity passed to save method
        ArgumentCaptor<PostEntity> postCaptor = ArgumentCaptor.forClass(PostEntity.class);
        verify(postCrudRepository).save(postCaptor.capture());
        PostEntity capturedPost = postCaptor.getValue();
        assertEquals(Status.PUBLISHED, capturedPost.getStatus());
    }

    @Test
    void post_ShouldHandleNullFeaturedImageUrl_WhenFeaturedImageUrlIsEmpty() {
        // Arrange
        PostRequestDto dtoWithoutFeaturedImage = PostRequestDto.builder()
                .title("Test Title")
                .slug("test-slug")
                .content("Test content")
                .excerpt(Optional.of("Test excerpt"))
                .featuredImageUrl(Optional.empty())
                .status(Optional.of(Status.PUBLISHED))
                .build();

        when(postRequestConverter.fromPostRequestToPostRequestDto(mockPostRequest))
                .thenReturn(mockPostRequestDto);
        when(postSanitizer.sanitize(mockPostRequestDto))
                .thenReturn(dtoWithoutFeaturedImage);
        when(categorySanitizer.toPlainText(anyString()))
                .thenReturn("sanitized-category");
        when(categoryGenerator.findOrCreateByName(anyList(), eq(mockUser)))
                .thenReturn(mockCategories);
        when(postGenerator.generateSlug(anyString(), anyString()))
                .thenReturn("generated-slug");
        when(postGenerator.getPublishedAt())
                .thenReturn(LocalDateTime.now());
        when(postCrudRepository.save(any(PostEntity.class)))
                .thenReturn(mockSavedPost);

        // Act
        PostEntity result = postArticleOperationPerformer.post(mockUser, mockPostRequest);

        // Assert
        assertNotNull(result);

        // Capture the PostEntity passed to save method
        ArgumentCaptor<PostEntity> postCaptor = ArgumentCaptor.forClass(PostEntity.class);
        verify(postCrudRepository).save(postCaptor.capture());
        PostEntity capturedPost = postCaptor.getValue();
        assertNull(capturedPost.getFeaturedImageUrl());
    }

    @Test
    void post_ShouldHandleEmptyCategories_WhenCategoriesIsNull() {
        // Arrange
        PostRequest requestWithoutCategories = PostRequest.builder()
                .title("Test Post Title")
                .slug("test-post-slug")
                .content("This is test content")
                .excerpt("Test excerpt")
                .featuredImageUrl("https://example.com/image.jpg")
                .status(Status.PUBLISHED)
                .categories(null)
                .build();

        when(postRequestConverter.fromPostRequestToPostRequestDto(requestWithoutCategories))
                .thenReturn(mockPostRequestDto);
        when(postSanitizer.sanitize(mockPostRequestDto))
                .thenReturn(sanitizedPostRequestDto);
        when(postGenerator.generateSlug(anyString(), anyString()))
                .thenReturn("generated-slug");
        when(postGenerator.getPublishedAt())
                .thenReturn(LocalDateTime.now());
        when(postCrudRepository.save(any(PostEntity.class)))
                .thenReturn(mockSavedPost);

        // Act
        PostEntity result = postArticleOperationPerformer.post(mockUser, requestWithoutCategories);

        // Assert
        assertNotNull(result);

        // Verify that category processing was skipped
        verify(categorySanitizer, never()).toPlainText(anyString());
        verify(categoryGenerator, never()).findOrCreateByName(anyList(), any(UserEntity.class));
    }

    @Test
    void post_ShouldHandleEmptyCategories_WhenCategoriesIsEmpty() {
        // Arrange
        PostRequest requestWithEmptyCategories = PostRequest.builder()
                .title("Test Post Title")
                .slug("test-post-slug")
                .content("This is test content")
                .excerpt("Test excerpt")
                .featuredImageUrl("https://example.com/image.jpg")
                .status(Status.PUBLISHED)
                .categories(Collections.emptyList())
                .build();

        when(postRequestConverter.fromPostRequestToPostRequestDto(requestWithEmptyCategories))
                .thenReturn(mockPostRequestDto);
        when(postSanitizer.sanitize(mockPostRequestDto))
                .thenReturn(sanitizedPostRequestDto);
        when(postGenerator.generateSlug(anyString(), anyString()))
                .thenReturn("generated-slug");
        when(postGenerator.getPublishedAt())
                .thenReturn(LocalDateTime.now());
        when(postCrudRepository.save(any(PostEntity.class)))
                .thenReturn(mockSavedPost);

        // Act
        PostEntity result = postArticleOperationPerformer.post(mockUser, requestWithEmptyCategories);

        // Assert
        assertNotNull(result);

        // Verify that category processing was skipped
        verify(categorySanitizer, never()).toPlainText(anyString());
        verify(categoryGenerator, never()).findOrCreateByName(anyList(), any(UserEntity.class));
    }

    @Test
    void post_ShouldVerifyPostEntityBuilder_WithCorrectValues() {
        // Arrange
        LocalDateTime fixedPublishedAt = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

        when(postRequestConverter.fromPostRequestToPostRequestDto(mockPostRequest))
                .thenReturn(mockPostRequestDto);
        when(postSanitizer.sanitize(mockPostRequestDto))
                .thenReturn(sanitizedPostRequestDto);
        when(categorySanitizer.toPlainText(anyString()))
                .thenReturn("sanitized-category");
        when(categoryGenerator.findOrCreateByName(anyList(), eq(mockUser)))
                .thenReturn(mockCategories);
        when(postGenerator.generateSlug("sanitized-test-post-slug", "Sanitized Test Post Title"))
                .thenReturn("final-generated-slug");
        when(postGenerator.getPublishedAt())
                .thenReturn(fixedPublishedAt);
        when(postCrudRepository.save(any(PostEntity.class)))
                .thenReturn(mockSavedPost);

        // Act
        PostEntity result = postArticleOperationPerformer.post(mockUser, mockPostRequest);

        // Assert
        ArgumentCaptor<PostEntity> postCaptor = ArgumentCaptor.forClass(PostEntity.class);
        verify(postCrudRepository).save(postCaptor.capture());
        PostEntity capturedPost = postCaptor.getValue();

        assertEquals("Sanitized Test Post Title", capturedPost.getTitle());
        assertEquals("final-generated-slug", capturedPost.getSlug());
        assertEquals("This is sanitized test content", capturedPost.getContent());
        assertEquals("Sanitized test excerpt", capturedPost.getExcerpt());
        assertEquals("https://example.com/image.jpg", capturedPost.getFeaturedImageUrl());
        assertEquals(Status.PUBLISHED, capturedPost.getStatus());
        assertEquals(mockUser, capturedPost.getUser());
        assertEquals(mockCategories, capturedPost.getCategories());
        assertEquals(fixedPublishedAt, capturedPost.getPublishedAt());
        assertNotNull(capturedPost.getCreatedAt());
    }
}