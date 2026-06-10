package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.SearchHistory;
import com.aivle.bookapp.domain.User;
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
        return searchHistoryRepository.findRecentTop5ByUserId(userId);
    }

    @Transactional
    public SearchHistory createSearchHistory(Long userId, String keyword) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        SearchHistory searchHistory = SearchHistory.builder()
                .user(user)
                .keyword(keyword)
                .build();
                
        return searchHistoryRepository.save(searchHistory);
    }
}
