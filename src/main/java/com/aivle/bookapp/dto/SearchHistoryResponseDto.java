package com.aivle.bookapp.dto;

import com.aivle.bookapp.domain.SearchHistory;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class SearchHistoryResponseDto {
    private Long id;
    private String keyword;
    private LocalDateTime created;

    public SearchHistoryResponseDto(SearchHistory history) {
        this.id = history.getId();
        this.keyword = history.getKeyword();
        this.created = history.getCreatedAt();
    }
}
