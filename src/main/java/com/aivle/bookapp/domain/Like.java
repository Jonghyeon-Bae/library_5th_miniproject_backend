// package com.aivle.bookapp.domain;

// import jakarta.persistence.*;
// import jakarta.validation.constraints.NotBlank;
// import lombok.Getter;
// import lombok.Setter;

// @Entity
// @Getter
// @Setter

// public class Like {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(nullable = false)
//     @NotBlank(message = "User ID is required for like")
//     private Long user_id;

//     @Column(nullable = false)
//     @NotBlank(message = "Book ID is required for like")
//     private Long book_id;

// }
