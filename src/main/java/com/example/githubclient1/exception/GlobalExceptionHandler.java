package com.example.githubclient1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RepositoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleRepositoryNotFound() {
    }
}