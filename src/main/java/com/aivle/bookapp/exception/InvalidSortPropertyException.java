package com.aivle.bookapp.exception;

/**
 * 정렬 기준으로 허용되지 않는 필드명이 요청되었을 때 사용한다.
 */
public class InvalidSortPropertyException extends RuntimeException {
    public InvalidSortPropertyException(String sortBy) {
        super("Invalid sort property: " + sortBy);
    }
}
