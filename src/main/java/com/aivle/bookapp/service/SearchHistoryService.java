package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.SearchHistory;
import com.aivle.bookapp.domain.User;
import com.aivle.bookapp.exception.UserNotFoundException;
import com.aivle.bookapp.repository.SearchHistoryRepository;
import com.aivle.bookapp.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final UsersRepository usersRepository;

    public List<SearchHistory> findRecentTop5(Long userId) {
        return searchHistoryRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public SearchHistory createSearchHistory(Long userId, String keyword) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // 동일 키워드 있을 경우 삭제
        searchHistoryRepository.deleteByUserIdAndKeyword(userId, keyword);

        SearchHistory searchHistory = SearchHistory.builder()
                .user(user)
                .keyword(keyword)
                .build();
                
        return searchHistoryRepository.save(searchHistory);
    }

    // 특정 사용자의 전체 검색 기록 조회
    public List<SearchHistory> findAllHistory(Long userId) {
        return searchHistoryRepository
                .findAllByUserIdOrderByCreatedAtDesc(userId);
    }

    // 특정 사용자의 검색 기록 전체 삭제(type -> long)
    @Transactional
    public long deleteAllHistory(Long userId) {
        usersRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return searchHistoryRepository.deleteAllByUserId(userId);
    }

    // 특정 사용자의 특정 검색 기록 삭제 (B2)
    @Transactional
    public void deleteHistory(Long userId, String keyword) {
        usersRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        
        searchHistoryRepository.deleteByUserIdAndKeyword(userId,keyword);
    }
}
