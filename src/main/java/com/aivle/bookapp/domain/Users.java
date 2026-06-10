package com.aivle.bookapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    private String password;

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

    public Users(String email, String password, String name, String avatar) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.avatar = avatar;
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

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeVerified() {
        this.verified = true;
    }

    public void updateAvatar(String avatarUrl) {
        this.avatar = avatarUrl;
    }

    public void changeEmailVisibility() {
        this.emailVisibility = !this.emailVisibility;
    }
}
