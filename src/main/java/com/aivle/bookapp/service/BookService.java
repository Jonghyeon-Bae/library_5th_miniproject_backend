package com.aivle.bookapp.service;

import java.util.List;

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

    @Transactional(readOnly = true)
    public Books findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

    }

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
    @Transactional(readOnly = true)
    public String getCount() {
        return String.valueOf(bookRepository.count());
    }

    @Transactional(readOnly = true)
    public List<Books> searchByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Transactional(readOnly = true)
    public List<Books> searchByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    @Transactional(readOnly = true)
    public List<Books> searchByTitleContaining(String keyword) {
        return bookRepository.findByTitleContaining(keyword);
    }

    @Transactional(readOnly = true)
    public List<Books> searchByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title, author);
    }

    @Transactional(readOnly = true)
    public List<String> authorGetTitle(String author) {
        List<Books> books = bookRepository.findByAuthor(author);
        return books.stream().map(b -> b.getTitle()).toList();
    }

    @Transactional(readOnly = true)
    public Page<Books> getPage(int page, int size, String sortBy) {
        Sort sort = Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return bookRepository.findAll(pageable);
    }

    @Transactional
    public Books createBook(Books book) {
        return bookRepository.save(book);
    }

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

    @Transactional
    public Books deleteBook(Long id) {
        Books book = findById(id);
        bookRepository.delete(book);
        return book;
    }
}
