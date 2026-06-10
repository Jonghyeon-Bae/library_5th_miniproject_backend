// SearchHistory 엔티티를 위한 리포지토리 인터페이스
package com.aivle.bookapp.repository;

import java.util.Comparator;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.aivle.bookapp.domain.SearchHistory;

public interface SearchHistoryRepository
        extends JpaRepository<SearchHistory, Long> {

    // 특정 사용자의 검색 기록 조회
    List<SearchHistory> findByUser_Id(Long userId);

    // 특정 사용자의 검색 기록 전체 삭제
    void deleteAllByUser_Id(Long userId);

    // 로그인한 사용자의 최근 검색어 5개 조회
    default List<SearchHistory> findRecentTop5ByUserId(Long userId) {
        return findByUser_Id(userId)
                .stream()
                .sorted(
                        Comparator.comparing(
                                SearchHistory::getCreatedAt,
                                Comparator.nullsLast(
                                        Comparator.reverseOrder()
                                )
                        )
                )
                .limit(5)
                .toList();
    }

    // 특정 사용자의 전체 검색 기록을 최신순으로 조회
    default List<SearchHistory> findAllByUserIdOrderByCreatedAtDesc(
            Long userId
    ) {
        return findByUser_Id(userId)
                .stream()
                .sorted(
                        Comparator.comparing(
                                SearchHistory::getCreatedAt,
                                Comparator.nullsLast(
                                        Comparator.reverseOrder()
                                )
                        )
                )
                .toList();
    }
}
