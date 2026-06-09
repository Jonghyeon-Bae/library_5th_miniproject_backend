package com.aivle.bookapp.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aivle.bookapp.domain.Book;
// import com.aivle.bookapp.repository.BookRepository;
import com.aivle.bookapp.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class BookController {

    // private final BookRepository bookRepository;
    private final BookService bookService;


    //[소한민] Response Entity 사용하여 명시적인 200 OK 반환/ 응답규격 통일
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBook(@PathVariable("id") Long id){
        return ResponseEntity.ok(bookService.findById(id));
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAll(){
        return ResponseEntity.ok(bookService.findAll());
    }

    // @DeleteMapping("/books/{id}")
    // public String deleteBook(@PathVariable("id") Long id){
    //     return bookService.deleteBook(id);
    // }

    @GetMapping("/books/count")
    public ResponseEntity<String> getCount() {
        return ResponseEntity.ok(bookService.getCount());
    }

    @GetMapping("/books/search/title")
    public ResponseEntity<List<Book>> searchByTitle(@RequestParam("title") String title) {
        return ResponseEntity.ok(bookService.searchByTitle(title));
    }

    @GetMapping("/books/search/author")
    public ResponseEntity<List<Book>> searchByAuthor(@RequestParam("author") String author) {
        return ResponseEntity.ok(bookService.searchByAuthor(author));
    }

    @GetMapping("/books/search")
    public ResponseEntity<List<Book>> searchByTitleContaining(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(bookService.searchByTitleContaining(keyword));
    }

    @GetMapping("/books/search/detail")
    public ResponseEntity<List<Book>> searchByTitleAndAuthor(@RequestParam("title") String title, @RequestParam("author") String author) {
        return ResponseEntity.ok(bookService.searchByTitleAndAuthor(title, author));
    }

    @GetMapping("/books/search/author/title")
    public ResponseEntity<List<String>> authorGetTitle(@RequestParam("author") String author) {
        return ResponseEntity.ok(bookService.authorGetTitle(author));
    }

    @GetMapping("/books/page")
    public ResponseEntity<Page<Book>> getPage(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sortBy") String sortBy) {
        return ResponseEntity.ok(bookService.getPage(page, size, sortBy));
    }

    // [소한민] @Valid를 통한 데이터 검증 강화
    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book saved = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // [소한민] 업데이트 로직에도 @Valid 추가하여 데이터 정합성 보장
    @PatchMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") Long id, @Valid @RequestBody Book book) {
        Book updated = bookService.updateBook(id, book);
        return ResponseEntity.ok(updated);
    }

    // [소한민] 삭제 성공 시 본문이 없는 204 No Content 반환 (REST 표준 준수)
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
