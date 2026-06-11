package com.aivle.bookapp.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "books")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //외래키 설정 1:M 관계, 책 등록자는 Not null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //외래키 설정, 1:M 관계, 대여자는 Null 허용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id")
    private User borrower;

    @Column(nullable = false)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "Author is required")
    private String author;

    @Column(columnDefinition = "TEXT")
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

    @Column(name = "ai_review", columnDefinition = "TEXT")
    private String aiReview;

    @Column(unique = true, length = 13)
    private String isbn13;

    @Column(length = 100)
    private String category;

    private int sales = 0;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 책을 처음 등록할 때 필요한 필수 정보들
    @Builder
    public Book(User user, String title, String author, String publisher, 
                String thumbnail, String category, String contents, String isbn13) {
        this.user = user;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.thumbnail = thumbnail;
        this.category = category;
        this.contents = contents;
        this.isbn13 = isbn13;
    }

    // 상태 변경 메서드(Setter 대체)

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    // 대출 처리 메서드
    public void borrowBook(User borrower) {
        this.isAvailable = false;
        this.borrower = borrower;
    }
    // 반납 처리 메서드
    public void returnBook() {
        this.isAvailable = true;
        this.borrower = null;
    }
    // AI 썸네일만 전용으로 업데이트하는 메서드
    public void updateThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    // 책의 메타 정보를 수정할 때 사용
    public void updateBookInfo(String title, String contents, String author,
                               String publisher, String thumbnail, Boolean isAvailable,
                               Boolean bestbook, String aiReview) {
        if (title != null) this.title = title;
        if (contents != null) this.contents = contents;
        if (author != null) this.author = author;
        if (publisher != null) this.publisher = publisher;
        if (thumbnail != null) this.thumbnail = thumbnail;
        if (isAvailable != null) this.isAvailable = isAvailable;
        if (bestbook != null) this.bestbook = bestbook;
        if (aiReview != null) this.aiReview = aiReview;
    }
}
