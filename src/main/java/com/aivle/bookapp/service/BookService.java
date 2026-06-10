package com.aivle.bookapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.exception.BookNotFoundException;
import com.aivle.bookapp.repository.BookRepository;
import com.aivle.bookapp.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UsersRepository usersRepository;

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

    // 특정 사용자가 등록한 도서 목록 페이징 처리하여 반환
    @Transactional(readOnly = true)
    public Page<Book> getPageByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return bookRepository.findByUserId(userId, pageable);
    }

    // 책 생성
    @Transactional
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    // 책 수정
    @Transactional
    public Book updateBook(Long id, Map<String, Object> payload) {
        Book existBook = findById(id);

        if (payload.containsKey("title")) {
            existBook.updateBookInfo((String) payload.get("title"), null, null, null, null, null, null, null);
        }
        if (payload.containsKey("contents")) {
            existBook.updateBookInfo(null, (String) payload.get("contents"), null, null, null, null, null, null);
        }
        if (payload.containsKey("author")) {
            existBook.updateBookInfo(null, null, (String) payload.get("author"), null, null, null, null, null);
        }
        if (payload.containsKey("publisher")) {
            existBook.updateBookInfo(null, null, null, (String) payload.get("publisher"), null, null, null, null);
        }
        if (payload.containsKey("thumbnail")) {
            existBook.updateBookInfo(null, null, null, null, (String) payload.get("thumbnail"), null, null, null);
        }
        if (payload.containsKey("bestbook")) {
            existBook.updateBookInfo(null, null, null, null, null, null, (Boolean) payload.get("bestbook"), null);
        }
        if (payload.containsKey("aiReview") || payload.containsKey("ai_review")) {
            String review = (String) (payload.containsKey("aiReview") ? payload.get("aiReview") : payload.get("ai_review"));
            existBook.updateBookInfo(null, null, null, null, null, null, null, review);
        }

        // 대출/반납 상태 처리
        if (payload.containsKey("isAvailable") || payload.containsKey("is_available")) {
            Boolean isAvailable = (Boolean) (payload.containsKey("isAvailable") ? payload.get("isAvailable") : payload.get("is_available"));
            Object borrowerIdObj = payload.containsKey("borrower_id") ? payload.get("borrower_id") : payload.get("borrowerId");

            if (isAvailable != null) {
                if (isAvailable) {
                    existBook.returnBook();
                } else {
                    if (borrowerIdObj != null) {
                        Long borrowerId = null;
                        if (borrowerIdObj instanceof Number) {
                            borrowerId = ((Number) borrowerIdObj).longValue();
                        } else if (borrowerIdObj instanceof String) {
                            borrowerId = Long.parseLong((String) borrowerIdObj);
                        }
                        
                        if (borrowerId != null) {
                            com.aivle.bookapp.domain.User borrower = usersRepository.findById(borrowerId)
                                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + borrowerIdObj));
                            existBook.borrowBook(borrower);
                        } else {
                            existBook.returnBook();
                        }
                    } else {
                        existBook.returnBook();
                    }
                }
            }
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
                .findByTitleContainingOrAuthorContaining(
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

        return bookRepository.findByUserId(
                userId,
                pageable
        );
    }

    // 내가 등록한 책 개수 조회
    @Transactional(readOnly = true)
    public long countMyBooks(Long userId) {
        return bookRepository.countByUserId(userId);
    }

    // 내가 대출한 책 조회
    @Transactional(readOnly = true)
    public Page<Book> findBorrowedBooks(
            Long borrowerId,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return bookRepository.findByBorrowerId(
                borrowerId,
                pageable
        );
    }

    // 내가 대출한 책 개수 조회
    @Transactional(readOnly = true)
    public long countBorrowedBooks(Long borrowerId) {
        return bookRepository.countByBorrowerId(borrowerId);
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
        return bookRepository.findTop10ByOrderByLikeCountDesc();
    }

    // 대출 가능 책 수
    @Transactional(readOnly = true)
    public long getAvailableBookCount() {
        return bookRepository.countByIsAvailableTrue();
    }

    // 대출 중 책 수
    @Transactional(readOnly = true)
    public long getBorrowedBookCount() {
        return bookRepository.countByIsAvailableFalse();
    }

    // 표지 이미지 업데이트
    @Transactional
    public Book updateCover(Long id, String coverDataUrl) {
        Book existBook = findById(id);
        existBook.updateBookInfo(null, null, null, null, coverDataUrl, null, null, null);
        return bookRepository.save(existBook);
    }
}


