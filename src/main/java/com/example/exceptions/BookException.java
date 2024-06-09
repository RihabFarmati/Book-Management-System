package com.example.exceptions;

public class BookException extends RuntimeException {

    private String errorCode;

    public BookException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

