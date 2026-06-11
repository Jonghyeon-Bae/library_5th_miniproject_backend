// SearchHistory 엔티티를 위한 리포지토리 인터페이스
package com.aivle.bookapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.aivle.bookapp.domain.SearchHistory;
import org.springframework.transaction.annotation.Transactional;

public interface SearchHistoryRepository
        extends JpaRepository<SearchHistory, Long> {

    // 특정 사용자의 검색 기록 조회
    List<SearchHistory> findByUserId(Long userId);

    // 특정 사용자의 검색 기록 전체 삭제 (수정B-3)
    @Transactional
    long deleteAllByUserId(Long userId);

    // 로그인한 사용자의 최근 검색어 5개를 최신순으로 조회
    List<SearchHistory> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);

    // 특정 사용자의 전체 검색 기록을 최신순으로 조회
    List<SearchHistory> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    
    // 특정 사용자의 특정 검색 기록 삭제 (B1)
    @Transactional
    void deleteByUserIdAndKeyword(Long userId, String keyword);
}
