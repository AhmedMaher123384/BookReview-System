package com.BookReview.System.Exception;

import org.springframework.http.HttpStatus;

public abstract class ApiBaseException extends  RuntimeException {
    public ApiBaseException(String message) {
        super(message);
    }

    public abstract HttpStatus getHttpStatus();
}
