package com.backendapp.cms.blogging.helper;


import com.backendapp.cms.blogging.contract.AuthenticatedUserContract;
import com.backendapp.cms.blogging.contract.CategoryGeneratorContract;
import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.repository.CategoryCrudRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CategoryGeneratorTest {

    @MockitoBean
    CategoryCrudRepository categoryCrudRepository;

    @Autowired
    CategoryGenerator categoryGenerator;

    // find ot create and generate category by name
    @Test
    @DisplayName("Find Of Create Categories shoud be functional")
    void findOrCreateByName_shouldBeFunctional() {
        when(categoryCrudRepository.findAllByNameInAndUser(
                CategoryGeneratorContract.COLLECTION_OF_CATEGORIES_NAME,
                AuthenticatedUserContract.BLOGGER_USER))
                .thenReturn(CategoryGeneratorContract.SET_OF_AVAILABLE_CATEGORIES);
        //CategoryGeneratorContract.SET_OF_UNAVAILABLE_UNSAVED_CATEGORIES) tidak bisa dimasukan ke saveAll karena mockito menganggap
        //kembalian dari saveAll juga set. memang aneh.
        when(categoryCrudRepository.saveAll(anySet()))
                .thenReturn(CategoryGeneratorContract.LIST_OF_UNAVAILABLE_SAVED_CATEGORIES);

        Set<CategoryEntity> actual = categoryGenerator.findOrCreateByName(
                CategoryGeneratorContract.LIST_OF_CATEGORIES_NAME,
                AuthenticatedUserContract.BLOGGER_USER
        );

        assertEquals(actual, CategoryGeneratorContract.SET_OF_CATEGORY_AVAILABLE_AND_UNAVAILABLE_SAVED);
    }
}
