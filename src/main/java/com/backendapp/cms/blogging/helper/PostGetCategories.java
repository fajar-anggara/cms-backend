package com.backendapp.cms.blogging.helper;

import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.repository.CategoryCrudRepository;
import com.backendapp.cms.users.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PostGetCategories {

    private final CategoryCrudRepository categoryCrudRepository;

    public PostGetCategories(CategoryCrudRepository categoryCrudRepository) {
        this.categoryCrudRepository = categoryCrudRepository;
    }

    public HashSet<CategoryEntity> byId(Optional<List<Long>> ids, UserEntity user) {
        List<Long> categoryIds = ids
                .orElseGet(ArrayList::new);
        return new HashSet<>(categoryCrudRepository.findAllByIdInAndUser(categoryIds, user));
    }
}
