package com.backendapp.cms.blogging.repository;

import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.users.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CategoryCrudRepository extends JpaRepository<CategoryEntity, Long> {
    Set<CategoryEntity> findAllByNameInAndUser(Collection<String> name, UserEntity user);

}
