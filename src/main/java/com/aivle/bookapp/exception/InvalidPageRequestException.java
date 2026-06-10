package com.aivle.bookapp.exception;

/**
 * 페이지 번호나 페이지 크기가 유효하지 않을 때 사용한다.
 */
public class InvalidPageRequestException extends RuntimeException {
    public InvalidPageRequestException(int page, int size) {
        super("Invalid page request: page=" + page + ", size=" + size);
    }
}
