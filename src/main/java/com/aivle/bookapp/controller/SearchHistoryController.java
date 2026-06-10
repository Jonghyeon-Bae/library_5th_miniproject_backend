package com.aivle.bookapp.controller;

import com.aivle.bookapp.domain.SearchHistory;
import com.aivle.bookapp.dto.SearchHistoryRequestDto;
import com.aivle.bookapp.dto.SearchHistoryResponseDto;
import com.aivle.bookapp.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search-history")
@RequiredArgsConstructor
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    @GetMapping
    public ResponseEntity<List<SearchHistoryResponseDto>> getRecentHistories(@RequestParam("userId") Long userId) {
        List<SearchHistory> histories = searchHistoryService.findRecentTop5(userId);
        List<SearchHistoryResponseDto> dtos = histories.stream()
                .map(SearchHistoryResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<SearchHistoryResponseDto> createHistory(@RequestBody SearchHistoryRequestDto requestDto) {
        SearchHistory history = searchHistoryService.createSearchHistory(requestDto.getUserId(), requestDto.getKeyword());
        return ResponseEntity.ok(new SearchHistoryResponseDto(history));
    }
}
