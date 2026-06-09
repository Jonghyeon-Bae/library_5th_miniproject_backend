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

    @GetMapping("/books/{id}")
    public Book getBook(@PathVariable("id") Long id){
        return bookService.findById(id);
    }

    @GetMapping("/books")
    public List<Book> getAll(){
        return bookService.findAll();
    }

    // @DeleteMapping("/books/{id}")
    // public String deleteBook(@PathVariable("id") Long id){
    //     return bookService.deleteBook(id);
    // }

    @GetMapping("/books/count")
    public String getCount() {
        return bookService.getCount();
    }

    @GetMapping("/books/search/title")
    public List<Book> searchByTitle(@RequestParam("title") String title){
        return bookService.searchByTitle(title);
    }

    @GetMapping("/books/search/author")
    public List<Book> searchByAuthor(@RequestParam("author") String author){
        return bookService.searchByAuthor(author);
    }
    @GetMapping("/books/search")
    public List<Book> searchByTitleContaining(@RequestParam("keyword") String keyword){
        return bookService.searchByTitleContaining(keyword);
    }
    @GetMapping("/books/search/detail")
    public List<Book> searchByTitleAndAuthor(@RequestParam("title") String title, @RequestParam("author") String author){
        return bookService.searchByTitleAndAuthor(title, author);
    }

    @GetMapping("/books/search/author/title")
    public List<String> authorGetTitle(@RequestParam("author") String author){
        return bookService.authorGetTitle(author);
    }

    @GetMapping("/books/page")
    public Page<Book> getPage(@RequestParam("page") int page, @RequestParam("size") int size,@RequestParam("sortBy") String sortBy){
        return bookService.getPage(page,size,sortBy);
    }

    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book saved =  bookService.createBook(book);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PatchMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") Long id,@RequestBody Book book) {
        Book updated =  bookService.updateBook(id,book);

        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable("id") Long id){
        Book deleted = bookService.deleteBook(id); 

        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }
}
