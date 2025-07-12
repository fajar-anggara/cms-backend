//package com.backendapp.cms.blogging.service;
//
//import com.backendapp.cms.blogging.dto.CategoryRequestDto;
//import com.backendapp.cms.blogging.entity.CategoryEntity;
//import com.backendapp.cms.blogging.helper.CategorySanitizer;
//import com.backendapp.cms.blogging.repository.CategoryCrudRepository;
//import com.backendapp.cms.users.entity.UserEntity;
//import lombok.AllArgsConstructor;
//import org.owasp.html.HtmlPolicyBuilder;
//import org.owasp.html.PolicyFactory;
//import org.springframework.stereotype.Service;
//
//@Service
//@AllArgsConstructor
//public class CreateCategoryOperationPerformer {
//
//    private CategoryCrudRepository categoryCrudRepository;
//    private CategorySanitizer categorySanitizer;
//
//    public CategoryEntity createCategory(CategoryRequestDto category, UserEntity user) {
//        CategoryRequestDto sanitizedCategory = CategoryRequestDto
//                .builder()
//                .name(category.getName()
//                        .map(categorySanitizer::toPlainText)
//                        .orElseThrow())
//                // cek apakah null? tapi tidak mungkin juga sih
//        // category tidak di xss
//    }
//}
