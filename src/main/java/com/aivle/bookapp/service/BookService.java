package com.aivle.bookapp.service;

import java.util.List;

import com.aivle.bookapp.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aivle.bookapp.domain.Books;
import com.aivle.bookapp.exception.BookNotFoundException;
import com.aivle.bookapp.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // 책(ID)를 통한 책 조회
    @Transactional(readOnly = true)
    public Books findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

    }

    // 모든 책 조회
    @Transactional(readOnly = true)
    public List<Books> findAll() {
        return bookRepository.findAll();
    }

    // public String deleteBook(Long id){
    //     if (bookRepository.existsById(id)){
    //         bookRepository.deleteById(id);
    //         return "Delete Success : "+id;
    //     }throw new RuntimeException("Book not Found : "+id);
    // }

    // 책 개수 조회
    @Transactional(readOnly = true)
    public String getCount() {
        return String.valueOf(bookRepository.count());
    }
    // 책 제목 조회
    @Transactional(readOnly = true)
    public List<Books> searchByTitle(String title) {
        return bookRepository.findByTitle(title);
    }
    // 책 저자 조회
    @Transactional(readOnly = true)
    public List<Books> searchByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }
    // 키워드를 통한 책 제목조회
    @Transactional(readOnly = true)
    public List<Books> searchByTitleContaining(String keyword) {
        return bookRepository.findByTitleContaining(keyword);
    }
    // 책 제목과 저자 동시 조회
    @Transactional(readOnly = true)
    public List<Books> searchByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title, author);
    }
    // 특정 작가의 도서 제목 목록 조회
    @Transactional(readOnly = true)
    public List<String> authorGetTitle(String author) {
        List<Books> books = bookRepository.findByAuthor(author);
        return books.stream().map(b -> b.getTitle()).toList();
    }
    // 정렬 기준(오름차순)으로 도서 목록을 페이징 처리하여 반환
    @Transactional(readOnly = true)
    public Page<Books> getPage(int page, int size, String sortBy) {
        Sort sort = Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return bookRepository.findAll(pageable);
    }
    // 책 생성
    @Transactional
    public Books createBook(Books book) {
        return bookRepository.save(book);
    }

    // 책 수정
    @Transactional
    public Books updateBook(Long id, Books book) {
        Books existBook = findById(id);
        if (book.getTitle() != null) {
            existBook.setTitle(book.getTitle());
        }
        if (book.getAuthor() != null) {
            existBook.setAuthor(book.getAuthor());
        }
        return bookRepository.save(existBook);
    }
    // 책 삭제
    @Transactional
    public Books deleteBook(Long id) {
        Books book = findById(id);
        bookRepository.delete(book);
        return book;
    }
}



