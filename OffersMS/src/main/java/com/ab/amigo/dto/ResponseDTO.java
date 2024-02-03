package com.ab.amigo.dto;

import com.ab.amigo.exception.Error;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record ResponseDTO<T>(
        T response,
        List<Error> errors
) {
    public ResponseDTO(T response) {
        this(response, new ArrayList<>());
    }

    public ResponseDTO(Error error) {
        this(null, error);
    }

    public ResponseDTO(T response, Error error) {
        this(response, new ArrayList<>());
        errors.add(error);
    }

    public boolean hasError() {
        return !errors.isEmpty();
    }

    public static String getErrorMsg(List<Error> errors) {
        return errors.stream().map(Error::message).collect(Collectors.joining("::"));
    }
}
