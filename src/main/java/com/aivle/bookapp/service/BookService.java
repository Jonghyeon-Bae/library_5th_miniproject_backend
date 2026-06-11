package com.aivle.bookapp.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.domain.User;
import com.aivle.bookapp.dto.BookCreateRequestDto;
import com.aivle.bookapp.exception.BookAlreadyExistsException;
import com.aivle.bookapp.exception.BookNotFoundException;
import com.aivle.bookapp.exception.UnauthorizedAccessException;
import com.aivle.bookapp.exception.UserNotFoundException;
import com.aivle.bookapp.repository.BookRepository;
import com.aivle.bookapp.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

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

    // 정렬 기준으로 도서 목록을 페이징 처리하여 반환
    @Transactional(readOnly = true)
    public Page<Book> getPage(int page, int size, String sortParam) {
        Sort sort = Sort.by("createdAt").descending(); // 기본값: 최신순

        if (sortParam != null) {
            if (sortParam.equals("-created") || sortParam.equals("-createdAt")) {
                sort = Sort.by("createdAt").descending();
            } else if (sortParam.equals("created") || sortParam.equals("createdAt")) {
                sort = Sort.by("createdAt").ascending();
            } else if (sortParam.equals("title")) {
                sort = Sort.by("title").ascending();
            } else if (sortParam.equals("-title")) {
                sort = Sort.by("title").descending();
            }
        }

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
    public Book createBook(BookCreateRequestDto dto, String userEmail) {
        //유저 찾기
        User user = usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        // ISBN 중복 확인 (ISBN이 제공된 경우에만 중복 확인 수행)
        if (dto.getIsbn13() != null && !dto.getIsbn13().trim().isEmpty()) {
            if (bookRepository.existsByIsbn13(dto.getIsbn13())) {
                throw new BookAlreadyExistsException(dto.getIsbn13());
            }
        }

        //DTO가 스스로 엔티티로 변환
        Book book = dto.toEntity(user);

        // 저장
        return bookRepository.save(book);
    }

    // 책 수정
    @Transactional
    public Book updateBook(Long id, Map<String, Object> payload) {
        Book existBook = findById(id);

        String title = (String) payload.get("title");
        String contents = (String) payload.get("contents");
        String author = (String) payload.get("author");
        String publisher = (String) payload.get("publisher");
        String thumbnail = (String) payload.get("thumbnail");
        Boolean bestbook = (Boolean) payload.get("bestbook");
        String aiReview = (String) (payload.containsKey("aiReview") ? payload.get("aiReview") : payload.get("ai_review"));

        existBook.updateBookInfo(title, contents, author, publisher, thumbnail, null, bestbook, aiReview);

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

        return existBook;
    }

    // 책 삭제(권한 검증 추가)
    @Transactional
    public Book deleteBook(Long id, String userEmail) { // userEmail 파라미터 추가
        Book book = findById(id);

        // 권한 검증: 책을 등록한 유저가 존재하고, 이메일이 다르면 예외 발생 (수정_종현_05 유저 널 가드 추가)
        if (book.getUser() != null && !book.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedAccessException();
        }

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

    // 통합 검색 기능 추가 (정렬 파라미터 반영)
    @Transactional(readOnly = true)
    public Page<Book> searchBooks(
            String keyword,
            int page,
            int size,
            String sortParam
    ) {
        Sort sort = Sort.by("createdAt").descending(); // 기본값: 최신순

        if (sortParam != null) {
            if (sortParam.equals("-created") || sortParam.equals("-createdAt")) {
                sort = Sort.by("createdAt").descending();
            } else if (sortParam.equals("created") || sortParam.equals("createdAt")) {
                sort = Sort.by("createdAt").ascending();
            } else if (sortParam.equals("title")) {
                sort = Sort.by("title").ascending();
            } else if (sortParam.equals("-title")) {
                sort = Sort.by("title").descending();
            }
        }

        Pageable pageable = PageRequest.of(page, size, sort);

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
    // 내가 대출한 책 목록 조회
    @Transactional(readOnly = true)
    public Page<Book> getPageByBorrowerId(Long borrowerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        return bookRepository.findByBorrowerId(borrowerId, pageable);
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

    // AI 표지 업데이트 (권한 검증 추가)
    @Transactional
    public Book updateCover(Long id, String coverDataUrl, String userEmail) { // userEmail 파라미터 추가
        Book book = findById(id);

        // 권한 검증: 본인이 등록한 책만 표지를 바꿀 수 있음 (수정_종현_05 유저 널 가드 추가)
        if (book.getUser() != null && !book.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedAccessException();
        }

        book.updateThumbnail(coverDataUrl);
        return book;
    }
}


