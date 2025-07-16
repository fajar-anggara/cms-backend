package com.backendapp.cms.blogging.helper;

import com.backendapp.cms.blogging.entity.PostEntity;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CreateSpecificationTest {
//
//    @Autowired
//    private final CreateSpecification createSpecification = new CreateSpecification();
//
//    @Mock
//    private Root<PostEntity> root;
//
//    @Mock
//    private CriteriaQuery<?> query;
//
//    @Mock
//    private CriteriaBuilder criteriaBuilder;
//
//    @Mock
//    private Expression<String> usernameExpression;
//
//    @Mock
//    private Expression<String> lowerUsername;
//
//    @Mock
//    private Predicate predicate;

//    @Test
//    void shouldBuildLikePredicateForUsername() {
//        String search = "fajar";
//        String delete = "ALL";
//        String searchPattern = "%" + search.toLowerCase() + "%";
//
//        // Simulasi pemanggilan root.get("username") -> expression -> lower -> like
//        when(root.get("username")).thenReturn(usernameExpression);
//        when(criteriaBuilder.lower(usernameExpression)).thenReturn(lowerUsername);
//        when(criteriaBuilder.like(lowerUsername, searchPattern)).thenReturn(predicate);
//
//        Specification<PostEntity> specification = new CreateSpecification().getSpecification(search, delete);
//        Predicate result = specification.toPredicate(root, query, criteriaBuilder);
//
//        assertEquals(predicate, result);
//
//    }
}
