package com.aivle.bookapp.exception;

/**
 * 이미 대여 중이거나 이용할 수 없는 도서를 대여하려고 할 때 사용한다.
 */
public class BookUnavailableException extends RuntimeException {
    public BookUnavailableException(Long bookId) {
        super("Book is unavailable: " + bookId);
    }
}
