package com.emadsolutions.LMS.Exceptions;

public class EmptyRequestException extends RuntimeException {
    public EmptyRequestException(String message) {
        super(message);
    }
}