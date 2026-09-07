package com.priyanshu.iims.exception;

public class InvalidStatusTransitionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}