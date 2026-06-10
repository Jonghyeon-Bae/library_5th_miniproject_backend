package com.aivle.bookapp.exception;

/**
 * 도서를 찾을 수 없을 때 사용한다
 */
public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(Long id) {
        super("Book not found: " + id);
    }
}
