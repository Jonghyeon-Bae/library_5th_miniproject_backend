package com.aivle.bookapp.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "BOOK")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //외래키 설정 1:M 관계, 책 등록자는 Not null
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    //외래키 설정, 1:M 관계, 대여자는 Null 허용
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id")
    private Users borrower;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Author is required")
    private String author;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Contents is required")
    private String contents;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Publisher is required")
    private String publisher;

    @Column(length = 255)
    private String thumbnail;

    @Column(nullable = false)
    private Boolean is_available;

    @Column(nullable = false)
    private Boolean bestbook;

    @Column(nullable = false)
    @Min(value = 0, message = "like_count must be at least 0")
    private int like_count;

    @Column(length = 511)
    private String ai_review;

    private String isbn13;

    @Column(length = 100)
    private String category;

    private int sales;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updated_at;

    public void increaseLikeCount() {
        this.like_count++;
    }

    public void decreaseLikeCount() {
        if (this.like_count > 0) {
            this.like_count--;
        }
    }

    // 대출 처리 메서드
    public void borrowBook(Users borrower) {
        this.is_available = false;
        this.borrower = borrower;
    }
    // 반납 처리 메서드
    public void returnBook() {
        this.is_available = true;
        this.borrower = null;
    }

    public void updateBook(String title, String contents, String author,
                           String publisher, String thumbnail, Boolean is_available,
                           Boolean bestbook, String ai_review) {
        this.title = title;
        this.contents = contents;
        this.author = author;
        this.publisher = publisher;
        this.thumbnail = thumbnail;
        this.is_available = is_available;
        this.bestbook = bestbook;
        this.ai_review = ai_review;
    }
}
