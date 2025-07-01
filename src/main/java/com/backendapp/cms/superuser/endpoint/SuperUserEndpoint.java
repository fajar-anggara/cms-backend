package com.backendapp.cms.superuser.endpoint;

import com.backendapp.cms.common.enums.Deleted;
import com.backendapp.cms.common.enums.SortBy;
import com.backendapp.cms.common.enums.SortOrder;
import com.backendapp.cms.openapi.dto.*;
import com.backendapp.cms.openapi.superuser.api.SuperuserControllerApi;
import com.backendapp.cms.superuser.converter.PaginationConverter;
import com.backendapp.cms.superuser.service.SuperuserUserGetterOperationPerformer;
import com.backendapp.cms.users.converter.UserConverter;
import com.backendapp.cms.users.converter.UserConverterImpl;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.repository.SuperuserCrudRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Delete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Slf4j
public class SuperUserEndpoint implements SuperuserControllerApi {

    private final SuperuserUserGetterOperationPerformer superuserUserGetter;
    private final SuperuserCrudRepository superuserCrudRepository;
    private final UserConverterImpl userConverterImpl;
    private final PaginationConverter paginationConverter;

    @Override
    public ResponseEntity<Success200Response> deleteSingleUser(Long id) {
        return SuperuserControllerApi.super.deleteSingleUser(id);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetAllUser200Response> getAllUser(String deleted, String sortBy, String sortOrder, Integer limit, Integer page, String search) {
        Page<UserEntity> users = superuserUserGetter.getUsers(
                search,
                Deleted.valueOf(deleted),
                SortBy.valueOf(sortBy).value, // table name
                SortOrder.valueOf(sortOrder).direction, // direction
                page,
                limit
        );
        log.info("getting user data {}", users.getContent().toString());

        List<UserResponse> userData = users.getContent()
                .stream()
                .map(userConverterImpl::toUserResponse)
                .toList();


        PaginationMetadata pagination = paginationConverter.toPaginationMetadata(
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.hasNext(),
                users.hasPrevious()
        );

        GetAllUser200Response response = new GetAllUser200Response();
        response.setSuccess(true);
        response.setMessage("Berhasil mengambil semua data user");
        response.setData(paginationConverter.toGetAllUser200ResponseAllOfData(userData, pagination));

        return ResponseEntity.ok(response);
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
