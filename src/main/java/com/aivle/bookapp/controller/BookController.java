package com.aivle.bookapp.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.aivle.bookapp.dto.BookResponseDto;
import com.aivle.bookapp.dto.PageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aivle.bookapp.domain.Book;
// import com.aivle.bookapp.repository.BookRepository;
import com.aivle.bookapp.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping("/api/books") // [소한민] 공통 URI Prefix 적용
@RequiredArgsConstructor
public class BookController {

    // private final BookRepository bookRepository;
    private final BookService bookService;


    //[소한민] Response Entity 사용하여 명시적인 200 OK 반환/ 응답규격 통일
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable("id") Long id){
        return ResponseEntity.ok(bookService.findById(id));
    }

    // ISBN 중복 확인 API
    @GetMapping("/check-isbn")
    public ResponseEntity<Boolean> checkIsbn(@RequestParam("isbn13") String isbn13) {
        return ResponseEntity.ok(bookService.existsByIsbn13(isbn13));
    }

    //[소한민] API 도서목록 검색 페이징/키워드 검색 통합
    @GetMapping
    public ResponseEntity<PageResponseDto<BookResponseDto>> getBooks(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "keyword", required = false) String keyword) {

        // 1. Service에서 Page<Book> 엔티티 결과를 가져옴
        Page<Book> bookPage = bookService.getPage(page, size, "createdAt");

        // 2. Page<Book> 엔티티 내부의 내용물들을 BookResponseDto로 싹 변환 (.map 활용)
        Page<BookResponseDto> dtoPage = bookPage.map(book -> new BookResponseDto(book));

        // 3. 변환된 데이터를 우리가 만든 커스텀 래퍼로 감싸서 반환
        PageResponseDto<BookResponseDto> response = new PageResponseDto<>(dtoPage);

        return ResponseEntity.ok(response);
    }

    // 특정 사용자가 등록한 도서 목록 조회 API
    @GetMapping("/user/{userId}")
    public ResponseEntity<PageResponseDto<BookResponseDto>> getBooksByUser(
            @PathVariable("userId") Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        
        Page<Book> bookPage = bookService.getPageByUserId(userId, page, size);
        Page<BookResponseDto> dtoPage = bookPage.map(BookResponseDto::new);
        return ResponseEntity.ok(new PageResponseDto<>(dtoPage));
    }

    // 인기 도서 TOP 10 랭킹 API
    @GetMapping("/ranking")
    public ResponseEntity<List<BookResponseDto>> getRanking() {
        List<Book> books = bookService.getTop10PopularBooks();
        List<BookResponseDto> dtos = books.stream().map(BookResponseDto::new).toList();
        return ResponseEntity.ok(dtos);
    }

    // @DeleteMapping("/books/{id}")
    // public String deleteBook(@PathVariable("id") Long id){
    //     return bookService.deleteBook(id);
    // }

    //[소한민] 파편화된 API 주석처리 (필요시 주석 해제하여 사용)
//    @GetMapping("/count")
//    public ResponseEntity<String> getCount() {
//        return ResponseEntity.ok(bookService.getCount());
//    }
//
//    @GetMapping("/search/title")
//    public ResponseEntity<List<Book>> searchByTitle(@RequestParam("title") String title) {
//        return ResponseEntity.ok(bookService.searchByTitle(title));
//    }
//
//    @GetMapping("/search/author")
//    public ResponseEntity<List<Book>> searchByAuthor(@RequestParam("author") String author) {
//        return ResponseEntity.ok(bookService.searchByAuthor(author));
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<List<Book>> searchByTitleContaining(@RequestParam("keyword") String keyword) {
//        return ResponseEntity.ok(bookService.searchByTitleContaining(keyword));
//    }
//
//    @GetMapping("/search/detail")
//    public ResponseEntity<List<Book>> searchByTitleAndAuthor(@RequestParam("title") String title, @RequestParam("author") String author) {
//        return ResponseEntity.ok(bookService.searchByTitleAndAuthor(title, author));
//    }
//
//    @GetMapping("/search/author/title")
//    public ResponseEntity<List<String>> authorGetTitle(@RequestParam("author") String author) {
//        return ResponseEntity.ok(bookService.authorGetTitle(author));
//    }
//
//    @GetMapping("/page")
//    public ResponseEntity<Page<Book>> getPage(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sortBy") String sortBy) {
//        return ResponseEntity.ok(bookService.getPage(page, size, sortBy));
//    }

    // [소한민] 신규 도서 등록 Location header 및 body 반영
    @PostMapping
    public ResponseEntity<Map<String, Object>> createBook(@Valid @RequestBody Book book) {
        Book saved = bookService.createBook(book);

        // Location 헤더 생성
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        // 응답 스펙에 맞춘 커스텀 바디 구성
        Map<String, Object> responseBody = Map.of(
                "id", saved.getId(),
                "message", "Successfully created"
        );

        return ResponseEntity.created(location).body(responseBody);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") Long id, @RequestBody Book book) {
        Book updated = bookService.updateBook(id, book);
        return ResponseEntity.ok(updated);
    }

    // [소한민] 삭제 성공 시 본문이 없는 204 No Content 반환 (REST 표준 준수)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // [소한민] AI 생성 표지 이미지 저장
    @PatchMapping("/{id}/cover")
    public ResponseEntity<Map<String, Object>> updateBookCover(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> request) {

        String coverDataUrl = request.get("coverDataUrl");
        // TODO: Service에 표지 이미지만 업데이트하는 로직 추가 필요
        // bookService.updateCover(id, coverDataUrl);

        // 예시 응답
        return ResponseEntity.ok(Map.of("id", id, "thumbnail", coverDataUrl));
    }
}
