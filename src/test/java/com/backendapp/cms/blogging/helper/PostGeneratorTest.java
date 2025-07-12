package com.backendapp.cms.blogging.helper;

import com.backendapp.cms.blogging.repository.PostCrudRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
public class PostGeneratorTest {

    @MockitoBean
    private PostCrudRepository postCrudRepository;

    @Autowired
    private PostGenerator postGenerator;

    @Test
    @DisplayName("Post generator. generateSlug should functional")
    public void generateSlug_shouldFunctional() {
        when(postCrudRepository.existsBySlug(PostGeneratorContract.UNGENERATED_SLUG_FROM_TITLE))
                .thenReturn(false);

        String generatedSlug = postGenerator.generateSlug(Optional.of(PostGeneratorContract.UNGENERATED_SLUG_FROM_TITLE), PostGeneratorContract.UNGENERATED_SLUG_FROM_TITLE);

        assertEquals(PostGeneratorContract.GENERATED_SLUG, generatedSlug, "Generated slug nya harus sama");
    }

    @Test
    @DisplayName("Post generator.generateExcerpt shoud functional")
    public void generateExcerpt_shouldFunctional() {

        String generatedExcerpt = postGenerator.generateExcerpt(PostGeneratorContract.UNGENERATED_EXCERPT_FROM_CONTENT);

        assertEquals(generatedExcerpt, PostGeneratorContract.GENERATED_EXCERPT, "This should generated excerpt");
    }
}