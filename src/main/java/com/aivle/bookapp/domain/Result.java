package com.aivle.bookapp.domain;

public class Result {
    private String status;
    private String message;
    
    public Result(String status, String message) {
        this.status = status;
        this.message = message;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getMessage() {
        return message;
    }
}
