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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostArticle200Response> postArticle(PostRequest postRequest) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostEntity article = postArticleOperationPerformer.post(user, postRequest);
        HashSet<CategoryEntity> categories = postGetCategories.byId(Optional.of(postRequest.getCategories()), user);






    }

}
