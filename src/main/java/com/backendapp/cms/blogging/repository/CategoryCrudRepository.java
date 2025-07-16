package com.backendapp.cms.blogging.repository;

import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.openapi.dto.CategoriesSimpleDTO;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CategoryCrudRepository extends JpaRepository<CategoryEntity, Long> {
    Set<CategoryEntity> findAllByNameInAndUser(Collection<String> name, UserEntity user);

    Optional<CategoryEntity> findByNameAndUser(String name, UserEntity user);

    Optional<CategoryEntity> findByIdAndUser(Long id, UserEntity user);

    Optional<List<CategoryEntity>> findAllByUserAndDeletedAtIsNull(UserEntity user);

    Optional<CategoryEntity> findByIdAndUserAndDeletedAtIsNull(Long id, UserEntity user);

    boolean existsByNameAndUser(@NotBlank(message = "Nama kategori tidak boleh kosong") String name, UserEntity user);
}
