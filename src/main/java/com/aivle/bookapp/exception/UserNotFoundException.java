package com.aivle.bookapp.exception;

/**
 * 요청한 이메일에 해당하는 사용자를 찾을 수 없을 때 사용한다.
 */

public class UserNotFoundException extends RuntimeException {

    // 이메일로 검색했는데 못 찾았을 때
    public UserNotFoundException(String email) {
        super("User not found with email: " + email);
    }

    //고유 ID로 검색했는데 못 찾았을 때
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
}