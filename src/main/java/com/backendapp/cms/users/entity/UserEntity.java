package com.backendapp.cms.users.entity;

import com.backendapp.cms.security.entity.UserGrantedAuthority;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "profile_picture", nullable = true)
    private String profilePicture;

    @Column(name = "bio", nullable = true)
    private String bio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authority_id", nullable = false)
    private UserGrantedAuthority authority;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "email_verified", nullable = true)
    private boolean emailVerified;

    @Column(name = "reset_password_token", nullable = true)
    private String resetPasswordToken;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.authority == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(this.authority);
    }

    @Override
    public String getPassword() {
        return password; // Mengembalikan password yang tersimpan
    }

    @Override
    public String getUsername() {
        return username; // Mengembalikan username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled; //
    }



    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserEntity that = (UserEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}