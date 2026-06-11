package com.aivle.bookapp.dto;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookCreateRequestDto {

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "author is required")
    private String author;

    @NotBlank(message = "isbn13 is required")
    private String isbn13;

    private String category;
    private String contents;
    private String publisher;
    private String thumbnail;

    // DTO의 데이터를 Book 엔티티로 변환하는 메서드 (모든 필드 매핑 완료)
    public Book toEntity(User user) {
        return Book.builder()
                .user(user)             // 서비스에서 조회해 온 등록자 정보
                .title(this.title)      // 프론트에서 넘어온 책 제목
                .author(this.author)    // 프론트에서 넘어온 저자
                .isbn13(this.isbn13)    // 프론트에서 넘어온 ISBN
                .category(this.category)
                .contents(this.contents)
                .publisher(this.publisher)
                .thumbnail(this.thumbnail)
                .build();
    }
}