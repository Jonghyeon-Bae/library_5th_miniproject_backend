package com.aivle.bookapp.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Books {
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

    @Column(nullable = false)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "Author is required")
    private String author;

    @Column(nullable = false)
    @NotBlank(message = "Contents is required")
    private String contents;

    @Column(nullable = false)
    @NotBlank(message = "Publisher is required")
    private String publisher;

    @Column(columnDefinition = "LONGTEXT")
    private String thumbnail;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    private Boolean bestbook = false;

    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;

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
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    // 대출 처리 메서드
    public void borrowBook(Users borrower) {
        this.isAvailable = false;
        this.borrower = borrower;
    }
    // 반납 처리 메서드
    public void returnBook() {
        this.isAvailable = true;
        this.borrower = null;
    }

    public void updateBook(String title, String contents, String author,
                           String publisher, String thumbnail, Boolean isAvailable,
                           Boolean bestbook, String ai_review) {
        this.title = title;
        this.contents = contents;
        this.author = author;
        this.publisher = publisher;
        this.thumbnail = thumbnail;
        this.isAvailable = isAvailable;
        this.bestbook = bestbook;
        this.ai_review = ai_review;
    }
}
