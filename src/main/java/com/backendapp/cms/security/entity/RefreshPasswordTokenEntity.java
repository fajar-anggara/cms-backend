package com.backendapp.cms.security.entity;

import com.backendapp.cms.users.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_password_token")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshPasswordTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    UserEntity user;

    @Column(name = "token", nullable = false)
    String token;

    @Column(name = "exp_date", nullable = false)
    LocalDateTime expDate;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expDate);
    }

}
