package com.backendapp.cms.blogging.helper;

import com.backendapp.cms.blogging.entity.CategoryEntity;
import com.backendapp.cms.blogging.entity.PostEntity;
import com.backendapp.cms.common.enums.Deleted;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.persistence.criteria.Join;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateSpecification {

    private final CategorySanitizer categorySanitizer;

    public Specification<PostEntity> getSpecification(String search, Deleted delete, String categorySLug, UserEntity user) {
        return Specification.allOf(
                createSearchSpecification(search),
                createSpecificationDelete(delete),
                createCategorySLug(categorySLug),
                createUserSpecification(user)
        );
    }

    private Specification<PostEntity> createSearchSpecification(String search) {
        if (search == null || search.trim().isEmpty()) {
            return null;
        }

        return ((root, cq, cb) -> {
            String searchPattern = "%" + search.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("title")), searchPattern);
        });
    }

    private Specification<PostEntity> createSpecificationDelete(Deleted delete) {
        if (delete == null) {
            delete = Deleted.NONDELETED;
        }

        Deleted finalDelete = delete;
        return ((root, query, criteriaBuilder) -> switch (finalDelete) {
            case DELETED -> criteriaBuilder.isNotNull(root.get("deletedAt"));
            case NONDELETED -> criteriaBuilder.isNull(root.get("deletedAt"));
            default -> criteriaBuilder.conjunction();
        });
    }

    private Specification<PostEntity> createCategorySLug(String categorySLug) {
        if (categorySLug == null || categorySLug.trim().isEmpty()) {
            return null;
        }

        String category = categorySanitizer.toSlugFormat(categorySanitizer.toPlainText(categorySLug));
        return ((root, query, cb) -> {
            Join<PostEntity, CategoryEntity> join = root.join("categories");
            return cb.equal(cb.lower(join.get("slug")), category);
        });
    }

    private Specification<PostEntity> createUserSpecification(UserEntity user) {
        // user tidak mungkin null

        return (((root, query, cb) -> {
            return cb.equal(root.get("user"), user);
        }));
    }
}
