package com.aivle.bookapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aivle.bookapp.domain.Books;

public interface BookRepository extends JpaRepository<Books, Long> {

    List<Books> findByTitle(String title);

    List<Books> findByAuthor(String author);

    List<Books> findByTitleContaining(String keyword);

    List<Books> findByTitleAndAuthor(String title, String author);
}
