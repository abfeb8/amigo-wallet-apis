package com.ab.amigo.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

import java.util.stream.Collectors;

public class ExceptionUtils {

    private ExceptionUtils() {
    }

    public static Error getRequestError(Errors errors) {
        return new Error(
                HttpStatus.NOT_ACCEPTABLE.value(),
                errors.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining("::"))
        );
    }

    public static Error getRequestError(Exception ex) {
        return new Error(
                HttpStatus.NOT_ACCEPTABLE.value(),
                ex.getMessage()
        );
    }
}
