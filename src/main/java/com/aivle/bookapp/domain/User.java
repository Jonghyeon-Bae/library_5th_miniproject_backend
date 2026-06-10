package com.aivle.bookapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    private String password;

    @Column(name = "token_key")
    private String tokenKey;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String avatar;

    @Column(name = "email_visibility")
    private Boolean emailVisibility = false;

    private Boolean verified = false;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Setter 대신 생성 시점에 Builder를 통해 값을 주입
    @Builder
    public User(String email, String password, String name, String avatar, String tokenKey, Boolean emailVisibility, Boolean verified) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.avatar = avatar;
        this.tokenKey = tokenKey;
        this.emailVisibility = emailVisibility != null ? emailVisibility : false;
        this.verified = verified != null ? verified : false;
    }

    public void updateProfile(String name, String avatar, Boolean emailVisibility) {
        if (name != null) {
            this.name = name;
        }
        if (avatar != null) {
            this.avatar = avatar;
        }
        if (emailVisibility != null) {
            this.emailVisibility = emailVisibility;
        }
    }

    public void updateTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void verifyAccount() {
        this.verified = true;
    }

    public void updateAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void changeEmailVisibility() {
        this.emailVisibility = (this.emailVisibility == null) ? true : !this.emailVisibility;
    }
}
