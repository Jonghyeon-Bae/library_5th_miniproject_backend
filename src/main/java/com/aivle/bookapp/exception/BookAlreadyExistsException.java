package com.aivle.bookapp.exception;

/**
 * 이미 등록된 ISBN13으로 도서를 추가하려고 할 때 사용한다.
 */
public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String isbn13) {
        super("Book already exists: " + isbn13);
    }
}
