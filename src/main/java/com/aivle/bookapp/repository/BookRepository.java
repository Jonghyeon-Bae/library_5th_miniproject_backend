package com.aivle.bookapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aivle.bookapp.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByTitleContaining(String keyword);
    List<Book> findByTitleAndAuthor(String title, String author);
}