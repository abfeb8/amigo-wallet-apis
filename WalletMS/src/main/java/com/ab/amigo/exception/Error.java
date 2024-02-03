package com.ab.amigo.exception;

public record Error(
        Integer errorCode,
        String message
) {
}
