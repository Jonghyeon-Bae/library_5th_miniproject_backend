package com.aivle.bookapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchHistoryRequestDto {
    private Long userId;
    private String keyword;
}
