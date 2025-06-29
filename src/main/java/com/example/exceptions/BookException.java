package com.example.exceptions;

import lombok.Getter;

@Getter
public class BookException extends RuntimeException {

    private final String errorCode;

    public BookException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}

