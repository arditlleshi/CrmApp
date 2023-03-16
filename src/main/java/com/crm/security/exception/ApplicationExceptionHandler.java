package com.crm.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException exception){
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(Exception exception){
        Map<String, String> errorMap = new HashMap<>();

        if (exception instanceof UserNotFoundException) {
            errorMap.put("error", exception.getMessage());
        } else if (exception instanceof ClientNotFoundException) {
            errorMap.put("error", exception.getMessage());
        } else if (exception instanceof EmailAlreadyExistsException) {
            errorMap.put("error", exception.getMessage());
        } else {
            errorMap.put("error", exception.getMessage());
        }
        return errorMap;
    }
}