package com.backendapp.cms.superuser.endpoint;

import com.backendapp.cms.common.enums.Deleted;
import com.backendapp.cms.common.enums.SortBy;
import com.backendapp.cms.common.enums.SortOrder;
import com.backendapp.cms.openapi.dto.*;
import com.backendapp.cms.openapi.superuser.api.SuperuserControllerApi;
import com.backendapp.cms.superuser.converter.PaginationConverter;
import com.backendapp.cms.superuser.service.SuperuserRegisterUserOperationPerformer;
import com.backendapp.cms.superuser.service.SuperuserUpdateOperationPerformer;
import com.backendapp.cms.superuser.service.SuperuserUserGetterOperationPerformer;
import com.backendapp.cms.users.converter.UserRegisterConverter;
import com.backendapp.cms.users.converter.UserResponseConverter;
import com.backendapp.cms.users.converter.UserResponseConverterImpl;
import com.backendapp.cms.users.converter.UserUpdateConverter;
import com.backendapp.cms.users.dto.UserRegisterDto;
import com.backendapp.cms.users.dto.UserUpdateDto;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.exception.UsernameOrEmailNotFoundException;
import com.backendapp.cms.users.repository.UserCrudRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class SuperUserEndpoint implements SuperuserControllerApi {

    private final SuperuserUserGetterOperationPerformer superuserUserGetter;
    private final UserResponseConverterImpl userConverterImpl;
    private final PaginationConverter paginationConverter;
    private final SuperuserRegisterUserOperationPerformer superuserRegisterUserOperationPerformer;
    private final UserResponseConverter userResponseConverter;
    private final UserCrudRepository userCrudRepository;
    private final UserRegisterConverter userRegisterConverter;
    private final UserUpdateConverter userUpdateConverter;
    private final SuperuserUpdateOperationPerformer superuserUpdateOperationPerformer;

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
                .map(userConverterImpl::fromUseEntityToUserResponse)
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateUser200Response> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserRegisterDto userRegisterDto = userRegisterConverter.fromCreateUserRequestToUserRegisterDto(request);
        UserEntity user = superuserRegisterUserOperationPerformer.registerUser(userRegisterDto);

        CreateUser200Response response = new CreateUser200Response();
        response.setSuccess(true);
        response.setMessage("Berhasi menambahkan user");
        response.setData(userResponseConverter.fromUseEntityToUserResponse(user));

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateUser200Response> getSingleUser(@Valid @RequestBody Long id) {
        UserEntity user = userCrudRepository.findById(id)
                .orElseThrow(UsernameOrEmailNotFoundException::new);
        UserResponse userResponse = userResponseConverter.fromUseEntityToUserResponse(user);

        CreateUser200Response response = new CreateUser200Response();
        response.setSuccess(true);
        response.setMessage("Berhasi mendaptkan user");
        response.setData(userResponse);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Success200Response> deleteSingleUser(Long id) {
        UserEntity user = userCrudRepository.findById(id)
                .orElseThrow(UsernameOrEmailNotFoundException::new);
        userCrudRepository.delete(user);

        return ResponseEntity.ok(new Success200Response(true, "Berhasil menghapus user"));
    }

    @Override
    public ResponseEntity<CreateUser200Response> updateSingleUser(Long id, UserUpdateRequest userUpdateRequest) {
        UserEntity user = userCrudRepository.findById(id)
                .orElseThrow(UsernameOrEmailNotFoundException::new);
        UserUpdateDto userUpdateDto = userUpdateConverter.fromUserUpdateRequestToUserUpdateDto(userUpdateRequest);
        UserEntity userUpdated = superuserUpdateOperationPerformer.updateUser(user, userUpdateDto);
        UserResponse userResponse = userResponseConverter.fromUseEntityToUserResponse(userUpdated);

        CreateUser200Response response = new CreateUser200Response();
        response.setSuccess(true);
        response.setMessage("Berhasil mengupdate user");
        response.setData(userResponse);

        return ResponseEntity.ok(response);
    }
}
