// package com.aivle.bookapp.domain;

// import jakarta.persistence.*;
// import jakarta.validation.constraints.NotBlank;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import org.springframework.data.annotation.CreatedDate;
// import org.springframework.data.annotation.LastModifiedDate;
// import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// import java.time.LocalDateTime;

// @Entity
// @Getter
// @NoArgsConstructor
// @AllArgsConstructor
// @EntityListeners(AuditingEntityListener.class)
// public class User {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(unique = true, nullable = false)
//     @NotBlank(message = "Email is required")
//     private String email;

//     @Column(nullable = false)
//     @NotBlank(message = "Password is required")
//     private String password;

//     @Column(nullable = false)
//     private String name;

//     private String avatar;

//     @Column(nullable = false)
//     private Boolean emailVisibility = false;

//     @Column(nullable = false)
//     private Boolean verified = false;

//     @CreatedDate
//     @Column(nullable = false, updatable = false)
//     private LocalDateTime created_at;

//     @LastModifiedDate
//     @Column(nullable = false)
//     private LocalDateTime updated_at;

//     public User(String email, String password, String name, String avatar) {
//         this.email = email;
//         this.password = password;
//         this.name = name;
//         this.avatar = avatar;
//         this.emailVisibility = false;
//         this.verified = false;
//     }

//     public void updateProfile(String name, String avatar, Boolean emailVisibility) {
//         if (name != null) this.name = name;
//         if (avatar != null) this.avatar = avatar;
//         if (emailVisibility != null) this.emailVisibility = emailVisibility;
//     }

//     public void changePassword(String newPassword) {
//         this.password = newPassword;
//     }

//     public void changeVerified() {
//         this.verified = true;
//     }

//     public void updateAvatar(String avatarUrl) {
//         this.avatar = avatarUrl;
//     }

//     public void changeEmailVisibility() {
//         this.emailVisibility = !this.emailVisibility;
//     }
// }