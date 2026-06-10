package com.aivle.bookapp.exception;

/**
 * 사용자가 이미 좋아요를 누른 도서에 다시 좋아요를 누르려고 할 때 사용한다.
 */
public class LikeAlreadyExistsException extends RuntimeException {
    public LikeAlreadyExistsException(Long bookId, Long userId) {
        super("Like already exists: bookId=" + bookId + ", userId=" + userId);
    }
}
