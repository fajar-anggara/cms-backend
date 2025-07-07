package com.backendapp.cms.blogging.entity;

import com.backendapp.cms.common.enums.Status;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "slug", unique = true)
    private String slug;

    @Column(name = "content", columnDefinition = "TEXT" , nullable = false)
    private String content;

    @Column(name = "excerpt")
    private String excerpt;

    @Column(name = "featured_image_url", columnDefinition = "TEXT")
    private String featuredImageUrl;

    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "posts_categories",
            joinColumns = @JoinColumn(name = "posts_id"),
            inverseJoinColumns = @JoinColumn(name = "categories_id")
    )
    private Set<CategoryEntity> categories = new HashSet<>();

    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;
}