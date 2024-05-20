package com.BookReview.System.Exception;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends ApiBaseException{

    public BookNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
