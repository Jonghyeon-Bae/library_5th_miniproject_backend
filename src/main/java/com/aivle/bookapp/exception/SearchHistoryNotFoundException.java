package com.aivle.bookapp.exception;

/**
 * 요청한 ID에 해당하는 검색 기록을 찾을 수 없을 때 사용한다.
 */
public class SearchHistoryNotFoundException extends RuntimeException {
    public SearchHistoryNotFoundException(Long search_id) {
        super("Search history not found: " + search_id);
    }
}
