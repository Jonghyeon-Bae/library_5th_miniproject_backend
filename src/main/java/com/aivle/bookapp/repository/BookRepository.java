// Book 엔티티를 위한 리포지토리 인터페이스
package com.aivle.bookapp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.aivle.bookapp.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

        // 제목이 정확히 일치하는 도서 조회
        List<Book> findByTitle(String title);

        // 저자가 정확히 일치하는 도서 조회
        List<Book> findByAuthor(String author);

        // 제목에 특정 키워드가 포함된 도서 조회
        List<Book> findByTitleContaining(String keyword);

        // 제목과 저자가 모두 정확히 일치하는 도서 조회
        List<Book> findByTitleAndAuthor(String title, String author);


        // 등록된 도서 검색_
        Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                String titleKeyword,
                String authorKeyword,
                Pageable pageable
        );

        // ISBN13 중복 등록 방지
        boolean existsByIsbn13(String isbn13);

        // ISBN13을 기준으로 등록된 도서 조회
        Optional<Book> findByIsbn13(String isbn13);

        // 마이페이지: 내가 등록한 책
        Page<Book> findByUser_Id(Long userId, Pageable pageable);

        // 마이페이지: 내가 등록한 책의 개수 조회
        long countByUser_Id(Long userId);

        // 마이페이지: 내가 대출한 책
        Page<Book> findByBorrower_Id(Long borrowerId, Pageable pageable);

        // 마이페이지: 내가 대출한 책의 개수 조회
        long countByBorrower_Id(Long borrowerId);

        // 강력 추천 도서 조회
        Page<Book> findByBestbookTrue(Pageable pageable);
        
        // 좋아요 수가 높은 인기 도서 TOP 10 조회 - 좌측 배너용
        List<Book> findTop10ByOrderByLikeCountDesc();

        // 대출 가능한 도서 수 조회 - 대시보드용
        long countByIsAvailableTrue();

        // 대출 중인 도서 수 조회 - 대시보드용
        long countByIsAvailableFalse();
}
