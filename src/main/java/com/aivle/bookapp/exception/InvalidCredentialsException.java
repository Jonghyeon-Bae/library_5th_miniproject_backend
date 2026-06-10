package com.aivle.bookapp.exception;

/**
 * 로그인 시 이메일 또는 비밀번호가 올바르지 않을 때 사용한다.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}
