package com.backendapp.cms.superuser.endpoint;

import com.backendapp.cms.openapi.dto.CreateUser200Response;
import com.backendapp.cms.openapi.dto.GetAllUser200Response;
import com.backendapp.cms.openapi.dto.Success200Response;
import com.backendapp.cms.openapi.dto.UserUpdateRequest;
import com.backendapp.cms.openapi.superuser.api.SuperuserControllerApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
public class SuperUserEndpoint implements SuperuserControllerApi {

    @Override
    public ResponseEntity<Success200Response> deleteSingleUser(Long id) {
        return SuperuserControllerApi.super.deleteSingleUser(id);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetAllUser200Response> getAllUser(String deleted, String sortBy, String sortOrder, Integer limit, String search) {

        return SuperuserControllerApi.super.getAllUser(deleted, sortBy, sortOrder, limit, search);
    }

    @Override
    public ResponseEntity<CreateUser200Response> getSingleUser(Long id) {
        return SuperuserControllerApi.super.getSingleUser(id);
    }

    @Override
    public ResponseEntity<CreateUser200Response> updateSingleUser(Long id, UserUpdateRequest userUpdateRequest) {
        return SuperuserControllerApi.super.updateSingleUser(id, userUpdateRequest);
    }
}
