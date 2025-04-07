package com.Mbakara.Banking_System.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public ResponseEntity<ExceptionResponse> handleAccountNotFoundException(AccountNotFoundException ex){
        return buildExceptionResponse(new ExceptionResponse(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    private ResponseEntity<ExceptionResponse> buildExceptionResponse(ExceptionResponse exceptionResponse) {
        return new ResponseEntity<>(exceptionResponse, exceptionResponse.getStatus());
    }
}
