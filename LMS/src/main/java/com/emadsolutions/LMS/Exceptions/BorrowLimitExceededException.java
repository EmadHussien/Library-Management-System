package com.emadsolutions.LMS.Exceptions;


public class BorrowLimitExceededException extends RuntimeException {
    public BorrowLimitExceededException(String message) {
        super(message);
    }
}

