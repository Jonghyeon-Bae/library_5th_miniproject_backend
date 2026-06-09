package com.aivle.bookapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aivle.bookapp.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByTitleContaining(String keyword);
    List<Book> findByTitleAndAuthor(String title, String author);
    List<Book> findByIsAvailableTrue(); // 대출 가능한 책만 조회
    List<Book> findByUserId(Long userId); // 특정 사용자가 등록한 책 조회
    List<Book> findByBorrowerId(Long borrowerId); // 특정 사용자가 빌린 책 조회
    List<Book> findByCategory(String category); // 도서 카테고리 검색
    List<Book> findByBestbookTrue(); // 강추 도서 조회
}