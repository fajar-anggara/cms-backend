package com.backendapp.cms.superuser.converter;

import com.backendapp.cms.openapi.dto.GetAllUser200Response;
import com.backendapp.cms.openapi.dto.GetAllUser200ResponseAllOfData;
import com.backendapp.cms.openapi.dto.PaginationMetadata;
import com.backendapp.cms.openapi.dto.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaginationConverter {
    public PaginationMetadata toPaginationMetadata(
            Integer currentPage,
            Integer pageSize,
            Long totalItems,
            Integer totalPages,
            boolean hasNextPage,
            boolean hasPreviousPage
    ) {
        return new PaginationMetadata(currentPage, pageSize, totalItems, totalPages, hasNextPage, hasPreviousPage);
    }

    public GetAllUser200ResponseAllOfData toGetAllUser200ResponseAllOfData(List<UserResponse> userResponses, PaginationMetadata pagination) {
        GetAllUser200ResponseAllOfData data = new GetAllUser200ResponseAllOfData();
        data.setUsers(userResponses);
        data.setPagination(pagination);

        return data;
    }
}