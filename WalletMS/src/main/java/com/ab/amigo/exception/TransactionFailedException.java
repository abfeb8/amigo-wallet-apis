package com.ab.amigo.exception;

public class TransactionFailedException extends RuntimeException {

    public TransactionFailedException(String errorMessage) {
        super(errorMessage);
    }
}
