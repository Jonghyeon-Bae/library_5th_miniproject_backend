package com.aivle.bookapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Contents is required")
    private String contents;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Author is required")
    private String author;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Publisher is required")
    private String publisher;

    @Column(length = 255)
    private String thumbnail;

    @Column(nullable = false)
    private Boolean isAvailable;

    @Column(nullable = false)
    private Boolean bestbook;

    @Column(nullable = false)
    @NotNull(message = "user_id is required")
    private Long userId;

    private Long borrowerId;

    @Column(nullable = false)
    @Min(value = 0, message = "like_count must be at least 0")
    private int likeCount;

    @Column(length = 511)
    private String aiReview;

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

    public void updateBook(String title, String contents, String author,
                           String publisher, String thumbnail, Boolean isAvailable,
                           Boolean bestbook, Long borrowerId, String aiReview) {
        this.title = title;
        this.contents = contents;
        this.author = author;
        this.publisher = publisher;
        this.thumbnail = thumbnail;
        this.isAvailable = isAvailable;
        this.bestbook = bestbook;
        this.borrowerId = borrowerId;
        this.aiReview = aiReview;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void setBorrower(Long borrowerId) {
        this.borrowerId = borrowerId;
        this.isAvailable = (borrowerId == null);
    }
}