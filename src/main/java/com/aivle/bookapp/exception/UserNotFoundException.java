package com.aivle.bookapp.exception;

/**
 * 요청한 이메일에 해당하는 사용자를 찾을 수 없을 때 사용한다.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("User not found with email: " + email);
    }
}
