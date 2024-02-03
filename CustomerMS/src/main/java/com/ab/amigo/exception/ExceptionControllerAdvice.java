package com.ab.amigo.exception;

import com.ab.amigo.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<String>> exceptionHandler(Exception ex) {
        return new ResponseEntity<>(
                new ResponseDTO<>("Failure", ExceptionUtils.getRequestError(ex)),
                HttpStatus.BAD_REQUEST
        );
    }
}
