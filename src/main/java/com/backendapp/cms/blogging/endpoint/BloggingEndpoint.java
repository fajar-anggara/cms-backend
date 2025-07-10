package com.backendapp.cms.blogging.endpoint;

import com.backendapp.cms.blogging.converter.PostResponseConverter;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.blogging.service.PostArticleOperationPerformer;
import com.backendapp.cms.openapi.blogging.api.BloggingControllerApi;
import com.backendapp.cms.openapi.dto.PostArticle200Response;
import com.backendapp.cms.openapi.dto.PostRequest;
import com.backendapp.cms.openapi.dto.PostSimpleResponse;
import com.backendapp.cms.openapi.dto.UserSimpleResponse;
import com.backendapp.cms.users.converter.UserResponseConverter;
import com.backendapp.cms.users.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class BloggingEndpoint implements BloggingControllerApi {

    private final PostArticleOperationPerformer postArticleOperationPerformer;
    private final PostResponseConverter postResponseConverter;
    private final UserResponseConverter userResponseConverter;

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostArticle200Response> postArticle(PostRequest postRequest) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserSimpleResponse userSimpleResponse = userResponseConverter.fromUserEntityToSimpleResponse(user);
        PostEntity article = postArticleOperationPerformer.post(user, postRequest);
        PostSimpleResponse postSimpleResponse = postResponseConverter.fromPostEntityToPostSimpleResponse(article);
        postSimpleResponse.setUser(userSimpleResponse);
        PostArticle200Response response = new PostArticle200Response();
        response.setSuccess(true);
        response.setMessage("Berhasil memposting sebuah artikel");
        response.setData(postSimpleResponse);
        log.info("article {}" , article);
        log.info(String.valueOf(postSimpleResponse));
        return ResponseEntity.ok(response);
    }
}
