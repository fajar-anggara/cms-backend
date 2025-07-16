package com.backendapp.cms.blogging.converter;

import com.backendapp.cms.blogging.contract.CategoryBuilder;
import com.backendapp.cms.blogging.contract.PostBuilder;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.openapi.dto.CategoryResponse;
import com.backendapp.cms.openapi.dto.CategoryResponseAllOfPosts;
import com.backendapp.cms.openapi.dto.GetAllCategories200ResponseAllOfData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CategoryResponseConverterTest {

    @Autowired
    private final CategoryResponseConverter categoryResponseConverter = new CategoryResponseConverterImpl();

    @Test
    @DisplayName("Should return CategorySimpleDto when received categoryEntity")
    void fromCategoriesEntityToCategorySimpleDto_shouldBeFunctional() {
        CategoryEntity categoryEntity = new CategoryBuilder().withDefault().build();
        CategoriesSimpleDTO categoriesSimpleDTO = new CategoryBuilder().withDefault().buildCategoriesSimpleDTO();

        CategoriesSimpleDTO actual = categoryResponseConverter.fromCategoriesEntityToCategorySimpleDto(categoryEntity);

        assertEquals(actual, categoriesSimpleDTO);
    }


//    @Test
//    @DisplayName("Should convert CategoryEntity to GetAllCategories200ResponseAllOfData correctly")
//    void fromCategoryEntityToGetAllCategories200ResponseAllOfData_shouldBeFunctional() {
//        CategoryEntity categoryEntity = new CategoryBuilder().withDefault().build();
//        GetAllCategories200ResponseAllOfData expected = new CategoryBuilder().withDefault().buildGetAllCategories200ResponseAllOfData();
//
//        GetAllCategories200ResponseAllOfData actual = categoryResponseConverter.fromCategoryEntityToGetAllCategories200ResponseAllOfData(categoryEntity);
//
//        assertEquals(expected, actual);
//    }

    @Test
    @DisplayName("Should convert PostEntity to CategoryResponseAllOfPosts correctly")
    void mapFromPostEntityToCategoryResponseAllOfPost_shouldBeFunctional() {
        PostEntity posts = new PostBuilder().withDefault().build();
        CategoryResponseAllOfPosts categoryResponseAllOfPosts = new PostBuilder().withDefault().buildCategoryResponseAllOfPosts();

        CategoryResponseAllOfPosts actual = categoryResponseConverter.mapFromPostEntityToCategoryResponseAllOfPost(posts);

        assertEquals(actual, categoryResponseAllOfPosts);
    }

    @Test
    @DisplayName("Should convert CategoryEntity to full CategoryResponse correctly")
    void fromCategoryEntityToCategoryResponse_shouldBeFunctional() {
        CategoryEntity categoryEntity = new CategoryBuilder().withDefault().build();
        CategoryResponse categoryResponse = new CategoryBuilder().withDefault().buildCategoryResponse();

        CategoryResponse actual = categoryResponseConverter.fromCategoryEntityToCategoryResponse(categoryEntity);

        assertEquals(actual, categoryResponse);


    }

}
