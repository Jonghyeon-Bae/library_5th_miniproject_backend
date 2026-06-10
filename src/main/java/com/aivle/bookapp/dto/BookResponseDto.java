package com.aivle.bookapp.dto;

import com.aivle.bookapp.domain.Book;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String thumbnail;
    private String contents;
    
    @JsonProperty("isAvailable")
    private Boolean isAvailable;
    
    @JsonProperty("borrower_id")
    private Long borrowerId;
    
    private Boolean bestbook;
    
    @JsonProperty("like_count")
    private int likeCount;
    
    @JsonProperty("ai_review")
    private String aiReview;
    
    @JsonProperty("user_id")
    private Long userId;
    
    private String isbn13;
    
    private LocalDateTime created;
    private LocalDateTime updated;

    // 엔티티를 DTO로 변환하는 생성자
    public BookResponseDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.thumbnail = book.getThumbnail();
        this.contents = book.getContents();
        this.isAvailable = book.getIsAvailable();
        this.borrowerId = book.getBorrower() != null ? book.getBorrower().getId() : null;
        this.bestbook = book.getBestbook();
        this.likeCount = book.getLikeCount();
        this.aiReview = book.getAiReview();
        this.userId = book.getUser() != null ? book.getUser().getId() : null;
        this.isbn13 = book.getIsbn13();
        this.created = book.getCreatedAt();
        this.updated = book.getUpdatedAt();
    }
}
