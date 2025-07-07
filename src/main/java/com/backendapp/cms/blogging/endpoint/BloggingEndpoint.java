package com.backendapp.cms.blogging.endpoint;

import com.backendapp.cms.blogging.converter.PostResponseConverter;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.blogging.service.PostArticleOperationPerformer;
import com.backendapp.cms.openapi.blogging.api.BloggingControllerApi;
import com.backendapp.cms.openapi.dto.PostArticle200Response;
import com.backendapp.cms.openapi.dto.PostRequest;
import com.backendapp.cms.openapi.dto.PostSimpleResponse;
import com.backendapp.cms.users.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BloggingEndpoint implements BloggingControllerApi {

    private final PostArticleOperationPerformer postArticleOperationPerformer;
    private final PostResponseConverter postResponseConverter;


    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostArticle200Response> postArticle(PostRequest postRequest) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostEntity article = postArticleOperationPerformer.post(user, postRequest);
        PostSimpleResponse postSimpleResponse = postResponseConverter.fromPostEntityToPostSimpleResponse(article);

        PostArticle200Response response = new PostArticle200Response();
        response.setSuccess(true);
        response.setMessage("Berhasil memposting sebuah artikel");
        response.setData(postSimpleResponse);

        return ResponseEntity.ok(response);
    }

}
