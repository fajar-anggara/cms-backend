package com.backendapp.cms.users.entity;

import com.backendapp.cms.security.entity.UserGrantedAuthority;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
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
// @RequiredArgsConstructor // <-- Hapus ini jika Anda punya @NoArgsConstructor dan @AllArgsConstructor
@NoArgsConstructor // Diperlukan oleh JPA
@AllArgsConstructor // Diperlukan oleh @Builder
@Builder // Diperlukan untuk method builder()
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "profile_picture", nullable = true)
    private String profilePicture;

    @Column(name = "bio", nullable = true)
    private String bio;

    @ManyToOne(fetch = FetchType.EAGER) // FetchType.EAGER karena role selalu dibutuhkan
    @JoinColumn(name = "authority_id", nullable = false) // Ini akan membuat kolom FK di tabel 'users'
    private UserGrantedAuthority authority; // Ganti nama field agar lebih jelas merujuk ke satu Authority

    @Column(name = "is_enable")
    private boolean isEnabled; // Defaultnya adalah false jika tidak diinisialisasi

    @Column(name = "is_email_verified", nullable = true)
    private boolean isEmailVerified; // Defaultnya adalah false jika tidak diinisialisasi

    @Column(name = "reset_password_token", nullable = true)
    private String resetPasswordToken;

    @CreatedDate
    @Column(nullable = false, updatable = false) // updatable = false untuk createdAt
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at") // Tambahkan nama kolom jika beda dari nama field
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = true) // Tambahkan nullable = true jika bisa null
    private LocalDateTime deletedAt;

    // --- IMPLEMENTASI WAJIB DARI USERDETAILS ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mengembalikan role/authority yang dimiliki pengguna.
        // Jika satu user hanya punya satu authority (ManyToOne ke UserGrantedAuthority),
        // gunakan Collections.singletonList. Jika bisa banyak, return List<UserGrantedAuthority>.
        if (this.authority == null) {
            return Collections.emptyList(); // Jika tidak ada authority, kembalikan list kosong
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
        // Implementasi logika expired account jika ada, default true jika tidak ada
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Implementasi logika lock account jika ada, default true jika tidak ada
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Implementasi logika expired kredensial/password jika ada, default true jika tidak ada
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Mengembalikan status enabled pengguna
        return isEnabled; // Menggunakan field isEnabled dari entity
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