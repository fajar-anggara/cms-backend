package com.backendapp.cms.superuser.service;

import com.backendapp.cms.common.enums.Deleted;
import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.repository.SuperuserCrudRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class SuperuserUserGetterOperationPerformer {

    private final SuperuserCrudRepository superuserCrudRepository;

    public Pageable getPageable(String sortBy, Sort.Direction direction, int page, int limit) {
        log.info("creating pageable");
        return PageRequest.of(page, limit, Sort.by(direction, sortBy));
    }

    public Page<UserEntity> getUsers(String search, Deleted deleted, String sortBy, Sort.Direction direction, int page, int limit) {
        Pageable pageable = getPageable(sortBy, direction, page, limit);
        Specification<UserEntity> specification = createSpecification(search, deleted);

        return superuserCrudRepository.findAll(specification, pageable);
    }

    private Specification<UserEntity> createSpecification(String search, Deleted deleted) {
        return Specification.allOf(createSearchSpecification(search), createDeletedSpecification(deleted));
    }

    private Specification<UserEntity> createSearchSpecification(String search) {
        log.info("creating search specification with search {}", search);
        if (search == null || search.trim().isEmpty()) {
            return null;
        }

        return ((root, query, criteriaBuilder) -> {
            String searchPattern = "%" + search.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), searchPattern);
        });
    }

    private Specification<UserEntity> createDeletedSpecification(Deleted deleted) {
        log.info("creating deleted specification with deleted  {}", deleted);
        return ((root, query, criteriaBuilder) -> switch (deleted) {
            case DELETED -> criteriaBuilder.isNotNull(root.get("deletedAt"));
            case NONDELETED -> criteriaBuilder.isNull(root.get("deletedAt"));
            default -> criteriaBuilder.conjunction();
        });
    }
}
