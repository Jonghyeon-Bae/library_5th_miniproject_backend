package com.aivle.bookapp.dto;

import com.aivle.bookapp.domain.Book;
import lombok.Getter;

@Getter
public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String thumbnail;
    // 필요에 따라 필드 추가 (예: isAvailable, category 등)

    // 엔티티를 DTO로 변환하는 생성자
    public BookResponseDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.thumbnail = book.getThumbnail();
    }
}
