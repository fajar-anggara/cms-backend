package com.backendapp.cms.blogging.repository;

import com.backendapp.cms.blogging.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCrudRepository extends JpaRepository<PostEntity, Long> {

    boolean existsBySlug(String slug);
}
