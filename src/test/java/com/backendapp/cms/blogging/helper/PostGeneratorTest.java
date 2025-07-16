package com.backendapp.cms.blogging.helper;

import com.backendapp.cms.blogging.contract.PostBuilder;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.blogging.repository.PostCrudRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostGeneratorTest {

    @MockitoBean
    private PostCrudRepository postCrudRepository;

    @Autowired
    private PostGenerator postGenerator;

    private final PolicyFactory plainText = new HtmlPolicyBuilder().toFactory();

    private String escapeRegex(String input) {
        // Escape special regex characters untuk MySQL
        return input.replaceAll("([\\[\\](){}*+?^$|\\\\.])", "\\\\$1");
    }

    @Test
    @DisplayName("Should return valid slug when given valid slug and valid title")
    void generateSlug_shouldReturnValidSlugWhenGivenValidSlugAndValidTitle() {

        PostEntity postEntity = new PostBuilder()
                .withDefault()
                .build();
        String regexPattern = "^" + escapeRegex(postEntity.getSlug()) + "(-[0-9]+)?$";
        when(postCrudRepository.findSlugPatternNative(postEntity.getSlug(), regexPattern))
                .thenReturn(new ArrayList<>());

        String actual = postGenerator.generateSlug(postEntity.getTitle());

        assertEquals(actual, postEntity.getSlug());
    }

    @Test
    @DisplayName("Should return valid slug when given null slug and valid title")
    void generateSlug_shouldReturnValidSlugWhenGivenNullSlugAndValidTitle() {
        PostEntity postEntity = new PostBuilder()
                .withDefault()
                .build();
        String regexPattern = "^" + escapeRegex(postEntity.getSlug()) + "(-[0-9]+)?$";
        when(postCrudRepository.findSlugPatternNative(postEntity.getSlug(), regexPattern))
                .thenReturn(new ArrayList<>());

        String actual = postGenerator.generateSlug(postEntity.getTitle());

        assertEquals(actual, postEntity.getSlug());
    }

    @Test
    @DisplayName("Should return valid slug when given null slug and null title")
    void generateSlug_shouldReturnValidSlugWhenGivenNullSlugAndNullTitle() {

        String actual = postGenerator.generateSlug(null);

        assertNotNull(actual);
    }

    @Test
    @DisplayName("Should return valid slug when given xss title")
    void generateSlug_shouldReturnValidSlugWhenGivenXssSlug() {
        PostEntity postEntity = new PostBuilder()
                .withDefault()
                .withXss()
                .build();
        String regexPattern = "^" + escapeRegex(postEntity.getSlug()) + "(-[0-9]+)?$";
        when(postCrudRepository.findSlugPatternNative(postEntity.getSlug(), regexPattern))
                .thenReturn(new ArrayList<>());

        String actual = postGenerator.generateSlug(postEntity.getTitle());

        assertEquals(actual, "my-title");
    }

    @Test
    @DisplayName("Should return slug plus repo count when there is slug on repo")
    void generatedSlug_shouldReturnValidSlugWhenGivenRepoCountIsMoreThan0() {
        PostEntity postEntity = new PostBuilder()
                .withDefault()
                .build();

        List<PostEntity> listOfPosts = new ArrayList<>();
        listOfPosts.add(postEntity);

        String title = plainText.sanitize(postEntity.getTitle()).toLowerCase();
        String regexPattern = "^" + escapeRegex(title) + "(-[0-9]+)?$";
        when(postCrudRepository.findSlugPatternNative(title, regexPattern))
                .thenReturn(listOfPosts);

        String actual = postGenerator.generateSlug(postEntity.getTitle());

        // karena menghasilkan time mili second jadi tidak bisa diprediksi
        assertNotNull(actual);
    }

    @Test
    @DisplayName("Should return valid excerpt when given 100 length content")
    void genrateExcerpt_shouldReturnValidExcerptWhenGiven100LengthContent() {
        PostEntity postEntity = new PostBuilder()
                .withDefault()
                .withContent("Melangkah tenang di tengah bising dunia, membawa makna tanpa perlu suara")
                .build();

        String actual = postGenerator.generateExcerpt(postEntity.getContent());

        assertEquals(actual, "Melangkah tenang di tengah bising dunia, membawa makna tanpa perlu suara");
    }

    @Test
    @DisplayName("Should return valid excerpt when given more than 100 length content")
    void generateExcerpt_shouldReturnValidExcerptWhenGivenMoreThan100LengthContent() {
        PostEntity postEntity = new PostBuilder()
                .withDefault()
                .withContent("Dalam sunyi pikiran, manusia sering menyusun makna dari serpihan realitas yang tidak pernah sepenuhnya jujur; namun justru di sanalah muncul keberanian untuk memahami, bukan untuk menilai.")
                .build();

        String actual = postGenerator.generateExcerpt(postEntity.getContent());

        assertEquals(actual, "Dalam sunyi pikiran, manusia sering menyusun makna dari serpihan realitas yang tidak pernah...");
    }
}