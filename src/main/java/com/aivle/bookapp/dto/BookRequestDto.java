package com.aivle.bookapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

// 추가_종현_01 Book 등록 요청을 위한 Request DTO 정의
@Getter
@Setter
public class BookRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    private String contents;

    @NotBlank(message = "Publisher is required")
    private String publisher;

    private String thumbnail;

    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    private Boolean bestbook;

    @JsonProperty("ai_review")
    private String aiReview;

    private String isbn13;

    private String category;

    private Integer sales;

    @JsonProperty("user_id")
    private Long userId;
}
