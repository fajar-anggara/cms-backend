package com.backendapp.cms.security.entity;

import com.backendapp.cms.users.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "jwt_refresh_token")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenEntity {
    @Id
    @NotNull
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    UserEntity user;

    @Column(name = "expired_time")
    Date expiredTime;

    public boolean isExpired() {
        return Date.from(Instant.now()).after(expiredTime);
    }

}
