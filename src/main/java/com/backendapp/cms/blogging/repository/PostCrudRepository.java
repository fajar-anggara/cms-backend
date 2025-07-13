package com.backendapp.cms.blogging.repository;

import com.backendapp.cms.blogging.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostCrudRepository extends JpaRepository<PostEntity, Long> {

    boolean existsBySlug(String slug);

    Optional<PostEntity> findSlug(String slug);

    @Query(value = "SELECT * FROM posts WHERE slug = ?1 OR slug REGEXP ?2",
            nativeQuery = true)
    List<PostEntity> findSlugPatternNative(String presentSlug, String regexPattern);
}
