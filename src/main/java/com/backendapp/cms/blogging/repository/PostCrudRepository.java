package com.backendapp.cms.blogging.repository;

import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostCrudRepository extends JpaRepository<PostEntity, Long> {

    boolean existsBySlug(String slug);

//    Optional<PostEntity> findBySlug(String slug);

    @Query(value = "SELECT * FROM posts WHERE slug = ?1 OR slug REGEXP ?2",
            nativeQuery = true)
    List<PostEntity> findSlugPatternNative(String presentSlug, String regexPattern);

    Optional<List<PostEntity>> findAllByUserAndDeletedAtIsNull(@NotNull UserEntity user);

    Optional<PostEntity> findByIdAndUser(Long id, UserEntity user);

    Optional<PostEntity> findByIdAndUserAndDeletedAtIsNull(Long id, UserEntity user);


    Page<PostEntity> findAll(Specification<PostEntity> specification, Pageable pageable);
}
