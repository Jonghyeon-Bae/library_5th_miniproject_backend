package com.aivle.bookapp.exception;

/**
 * 회원가입 시 이미 사용 중인 이메일로 가입하려고 할 때 사용한다.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
    }
}
