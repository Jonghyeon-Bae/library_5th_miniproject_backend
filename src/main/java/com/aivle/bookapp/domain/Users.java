package com.aivle.bookapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="USERS")
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

    @Column(nullable = false)
    private String name;

    private String avatar;

    @Column(nullable = false)
    private Boolean email_visibility = false;

    @Column(nullable = false)
    private Boolean verified = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updated_at;

    public Users(String email, String password, String name, String avatar) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.avatar = avatar;
        this.email_visibility = false;
        this.verified = false;
    }

    public void updateProfile(String name, String avatar, Boolean email_visibility) {
        if (name != null) this.name = name;
        if (avatar != null) this.avatar = avatar;
        if (email_visibility != null) this.email_visibility = email_visibility;
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
        this.email_visibility = !this.email_visibility;
    }
}