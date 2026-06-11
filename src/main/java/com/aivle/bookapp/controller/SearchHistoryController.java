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

    // 특정 사용자의 검색 기록 전체 삭제 (추가B-2)
    @DeleteMapping
    public ResponseEntity<String> deleteAllHistories(@RequestParam("userId") Long userId) {
         long deletedCount = searchHistoryService.deleteAllHistory(userId);
        return ResponseEntity.ok("사용자 검색 기록 전체 삭제 완료 : " + deletedCount + "개 삭제");
    }
}
