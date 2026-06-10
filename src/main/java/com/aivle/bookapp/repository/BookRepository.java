// Book 엔티티를 위한 리포지토리 인터페이스
package com.aivle.bookapp.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aivle.bookapp.domain.Books;

public interface BookRepository extends JpaRepository<Books, Long> {

    List<Books> findByTitle(String title);
    List<Books> findByAuthor(String author);
    List<Books> findByTitleContaining(String keyword);
    List<Books> findByTitleAndAuthor(String title, String author);

    // 등록된 도서 검색
    Page<Books> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String titleKeyword,
            String authorKeyword,
            Pageable pageable
    );

    // ISBN13 중복 등록 방지
    boolean existsByIsbn13(String isbn13);

    // ISBN13을 기준으로 등록된 도서 조회
    Optional<Books> findByIsbn13(String isbn13);

    // 마이페이지: 내가 등록한 책
    Page<Books> findByUser_Id(Long userId, Pageable pageable);

    // 마이페이지: 내가 등록한 책의 개수 조회
    long countByUser_Id(Long userId);

    // 마이페이지: 내가 대출한 책
    Page<Books> findByBorrower_Id(Long borrowerId, Pageable pageable);

    // 마이페이지: 내가 대출한 책의 개수 조회
    long countByBorrower_Id(Long borrowerId);

    // 강력 추천 도서 조회
    Page<Books> findByBestbookTrue(Pageable pageable);

    // 좌측 배너 인기 도서 TOP 10
    default List<Books> findTop10PopularBooks() {
        return findAll()
                .stream()
                .sorted(
                        Comparator.comparingInt(Books::getLikeCount)
                                .reversed()
                )
                .limit(10)
                .toList();
    }

    // 대시보드용 대출 가능 도서 수 조회
    default long countAvailableBooks() {
        return findAll()
                .stream()
                .filter(books -> Boolean.TRUE.equals(books.getIsAvailable()))
                .count();
    }

    // 대시보드용 대출 중인 도서 수 조회
    default long countBorrowedBooks() {
        return findAll()
                .stream()
                .filter(books -> Boolean.FALSE.equals(books.getIsAvailable()))
                .count();
    }
}
