package com.aivle.bookapp.exception;

/**
 * 대여 중이 아닌 도서를 반납하려고 할 때 사용한다.
 */
public class BookNotBorrowedException extends RuntimeException {
    public BookNotBorrowedException(Long bookId) {
        super("Book is not borrowed: " + bookId);
    }
}
