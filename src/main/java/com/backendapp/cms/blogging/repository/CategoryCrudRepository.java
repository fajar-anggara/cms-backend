package com.backendapp.cms.blogging.repository;

import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.users.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface CategoryCrudRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findAllByUser(UserEntity user);

    Set<CategoryEntity> findAllByIdInAndAuthor(Collection<Long> ids, UserEntity author);
}
