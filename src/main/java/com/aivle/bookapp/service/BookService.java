package com.aivle.bookapp.service;

import java.util.List;

import com.aivle.bookapp.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.exception.BookNotFoundException;
import com.aivle.bookapp.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // 책(ID)를 통한 책 조회
    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    // 모든 책 조회
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    // 책 개수 조회
    @Transactional(readOnly = true)
    public String getCount() {
        return String.valueOf(bookRepository.count());
    }

    // 책 제목 조회
    @Transactional(readOnly = true)
    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    // 책 저자 조회
    @Transactional(readOnly = true)
    public List<Book> searchByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    // 키워드를 통한 책 제목조회
    @Transactional(readOnly = true)
    public List<Book> searchByTitleContaining(String keyword) {
        return bookRepository.findByTitleContaining(keyword);
    }

    // 책 제목과 저자 동시 조회
    @Transactional(readOnly = true)
    public List<Book> searchByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title, author);
    }

    // 특정 작가의 도서 제목 목록 조회
    @Transactional(readOnly = true)
    public List<String> authorGetTitle(String author) {
        List<Book> books = bookRepository.findByAuthor(author);
        return books.stream().map(b -> b.getTitle()).toList();
    }

    // 정렬 기준(오름차순)으로 도서 목록을 페이징 처리하여 반환
    @Transactional(readOnly = true)
    public Page<Book> getPage(int page, int size, String sortBy) {
        Sort sort = Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return bookRepository.findAll(pageable);
    }

    // 책 생성
    @Transactional
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    // 책 수정
    @Transactional
    public Book updateBook(Long id, Book book) {
        Book existBook = findById(id);
        if (book.getTitle() != null) {
            existBook.updateBookInfo(book.getTitle(), null, null, null, null, null, null, null);
        }
        if (book.getAuthor() != null) {
            existBook.updateBookInfo(null, null, book.getAuthor(), null, null, null, null, null);
        }
        return bookRepository.save(existBook);
    }

    // 책 삭제
    @Transactional
    public Book deleteBook(Long id) {
        Book book = findById(id);
        bookRepository.delete(book);
        return book;
    }

    // ISBN 중복 검사
    @Transactional(readOnly = true)
    public boolean existsByIsbn13(String isbn13) {
        return bookRepository.existsByIsbn13(isbn13);
    }

    // ISBN 조회
    @Transactional(readOnly = true)
    public Book findByIsbn13(String isbn13) {

        return bookRepository.findByIsbn13(isbn13)
                .orElseThrow(
                        () -> new RuntimeException("도서를 찾을 수 없습니다.")
                );
    }

    // 통합 검색 기능 추가 (Controller에 통합 검색 API 추가)
    @Transactional(readOnly = true)
    public Page<Book> searchBooks(
            String keyword,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                        keyword,
                        keyword,
                        pageable
                );
    }

    // 내가 등록한 책 조회
    @Transactional(readOnly = true)
    public Page<Book> findMyBooks(
            Long userId,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return bookRepository.findByUser_Id(
                userId,
                pageable
        );
    }

    // 내가 등록한 책 개수 조회
    @Transactional(readOnly = true)
    public long countMyBooks(Long userId) {
        return bookRepository.countByUser_Id(userId);
    }

    // 내가 대출한 책 조회
    @Transactional(readOnly = true)
    public Page<Book> findBorrowedBooks(
            Long borrowerId,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return bookRepository.findByBorrower_Id(
                borrowerId,
                pageable
        );
    }

    // 내가 대출한 책 개수 조회
    @Transactional(readOnly = true)
    public long countBorrowedBooks(Long borrowerId) {
        return bookRepository.countByBorrower_Id(borrowerId);
    }

    // 강추 도서 조회
    @Transactional(readOnly = true)
    public Page<Book> findBestBooks(
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return bookRepository.findByBestbookTrue(
                pageable
        );
    }

    // 인기 도서 TOP10
    @Transactional(readOnly = true)
    public List<Book> getPopularBooks() {
        return bookRepository.findTop10PopularBooks();
    }

    // 대출 가능 책 수
    @Transactional(readOnly = true)
    public long getAvailableBookCount() {
        return bookRepository.countAvailableBooks();
    }

    // 대출 중 책 수
    @Transactional(readOnly = true)
    public long getBorrowedBookCount() {
        return bookRepository.countBorrowedBooks();
    }
}



