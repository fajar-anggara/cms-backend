package com.backendapp.cms.blogging.service;

import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.blogging.helper.CreatePageable;
import com.backendapp.cms.blogging.helper.CreateSpecification;
import com.backendapp.cms.blogging.repository.PostCrudRepository;
import com.backendapp.cms.common.enums.Deleted;
import com.backendapp.cms.common.enums.SortBy;
import com.backendapp.cms.common.enums.SortOrder;
import com.backendapp.cms.users.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllArticleOperationPerformer {

    private final CreatePageable createPageable;
    private final CreateSpecification createSpecification;
    private final PostCrudRepository postCrudRepository;

    public Page<PostEntity> getAll(
            Deleted deleted,
            int limit,
            String sortBy,
            Sort.Direction direction,
            String categorySlug,
            int page,
            String search,
            UserEntity user
    ) {
        Pageable pageable = createPageable.get(sortBy, direction, page, limit );
        Specification<PostEntity> specification = createSpecification.getSpecification(search, deleted, categorySlug, user);

        return postCrudRepository.findAll(specification, pageable);
    }

}
