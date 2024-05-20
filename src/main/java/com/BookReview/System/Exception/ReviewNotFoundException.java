package com.BookReview.System.Exception;

import org.springframework.http.HttpStatus;

public class ReviewNotFoundException extends ApiBaseException{

    public ReviewNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
