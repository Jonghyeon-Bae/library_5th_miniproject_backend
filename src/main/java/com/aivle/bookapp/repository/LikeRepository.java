// Likes 엔티티를 위한 리포지토리 인터페이스
package com.aivle.bookapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aivle.bookapp.domain.Likes;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    // 특정 사용자가 누른 모든 좋아요 조회
    List<Likes> findByUsers_Id(Long userId);

    // 특정 사용자가 특정 도서에 좋아요를 눌렀는지 확인
    boolean existsByBook_IdAndUsers_Id(Long bookId, Long userId);

    // 특정 사용자가 특정 도서에 누른 좋아요 조회
    Optional<Likes> findByBook_IdAndUsers_Id(Long bookId, Long userId);

    // 특정 사용자가 특정 도서에 누른 좋아요 취소
    void deleteByBook_IdAndUsers_Id(Long bookId, Long userId);

    // 특정 도서의 전체 좋아요 개수 조회
    long countByBook_Id(Long bookId);

    // 도서 삭제 시 해당 도서의 좋아요 기록 전체 삭제
    void deleteAllByBook_Id(Long bookId);
}
