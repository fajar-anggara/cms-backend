package com.backendapp.cms.blogs.service;

import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.blogging.exception.NoPostsException;
import com.backendapp.cms.blogging.helper.CreatePageable;
import com.backendapp.cms.blogging.helper.CreateSpecification;
import com.backendapp.cms.blogging.repository.PostCrudRepository;
import com.backendapp.cms.common.enums.Deleted;
import com.backendapp.cms.common.enums.SortBy;
import com.backendapp.cms.common.enums.SortOrder;
import com.backendapp.cms.users.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetArticlesOperationPerformer {

    private final PostCrudRepository postCrudRepository;
    private final CreatePageable createPageable;
    private final CreateSpecification createSpecification;

    public Page<PostEntity> getAllPosts(
            UserEntity user,
            int limit,
            int page,
            String sortBy,
            Sort.Direction direction ,
            String categorySlug,
            String search
    ) {
        Pageable pageable = createPageable.get(sortBy, direction, page, limit );
        Specification<PostEntity> specification = createSpecification.getSpecification(search, Deleted.NONDELETED, categorySlug, user);

        return postCrudRepository.findAll(specification, pageable);
    }
}
