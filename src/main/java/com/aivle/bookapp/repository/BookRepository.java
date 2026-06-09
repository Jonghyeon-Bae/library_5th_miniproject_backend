// Book 엔티티를 위한 리포지토리 인터페이스
package com.aivle.bookapp.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.aivle.bookapp.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    // 등록된 도서 검색
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

    // 좌측 배너 인기 도서 TOP 10
    default List<Book> findTop10PopularBooks() {
        return findAll()
                .stream()
                .sorted(
                        Comparator.comparingInt(Book::getLike_count)
                                .reversed()
                )
                .limit(10)
                .toList();
    }

    // 대시보드용 대출 가능 도서 수 조회
    default long countAvailableBooks() {
        return findAll()
                .stream()
                .filter(book -> Boolean.TRUE.equals(book.getIs_available()))
                .count();
    }

    // 대시보드용 대출 중인 도서 수 조회
    default long countBorrowedBooks() {
        return findAll()
                .stream()
                .filter(book -> Boolean.FALSE.equals(book.getIs_available()))
                .count();
    }
}
