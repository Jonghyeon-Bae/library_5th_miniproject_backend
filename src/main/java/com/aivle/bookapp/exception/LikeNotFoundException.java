package com.aivle.bookapp.exception;

/**
 * 취소하려는 좋아요 기록을 찾을 수 없을 때 사용한다.
 */
public class LikeNotFoundException extends RuntimeException {
    public LikeNotFoundException(Long book_id, Long user_id) {
        super("Like not found: book_id=" + book_id + ", user_id=" + user_id);
    }
}
