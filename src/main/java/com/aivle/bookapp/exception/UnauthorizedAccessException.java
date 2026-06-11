package com.aivle.bookapp.exception;

/**
 * 작성자 본인이 아니거나 권한이 없는 작업(수정/삭제)을 시도할 때 사용한다.
 */
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super("You do not have permission to modify or delete this resource.");
    }
}