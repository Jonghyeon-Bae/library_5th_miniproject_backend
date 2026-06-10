// Likes 엔티티를 위한 리포지토리 인터페이스
package com.aivle.bookapp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.aivle.bookapp.domain.Like;
import org.springframework.transaction.annotation.Transactional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // 특정 사용자가 누른 모든 좋아요 조회
    List<Like> findByUserId(Long userId);

    // 특정 사용자가 특정 도서에 좋아요를 눌렀는지 확인
    boolean existsByBookIdAndUserId(Long bookId, Long userId);

    // 특정 사용자가 특정 도서에 누른 좋아요 조회
    Optional<Like> findByBookIdAndUserId(Long bookId, Long userId);

    // 특정 사용자가 특정 도서에 누른 좋아요 취소
    @Transactional
    void deleteByBookIdAndUserId(Long bookId, Long userId);

    // 특정 도서의 전체 좋아요 개수 조회
    long countByBookId(Long bookId);

    // 도서 삭제 시 해당 도서의 좋아요 기록 전체 삭제 - 데이터베이스 쪽에서 삭제시 자동으로 삭제 되게 처리되어있음 기능으로만 존재
    @Transactional
    void deleteAllByBookId(Long bookId);
}
