package com.BookReview.System.Exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends ApiBaseException {

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}