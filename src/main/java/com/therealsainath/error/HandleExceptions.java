package com.therealsainath.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice(value = "com.therealsainath.controller")
@Slf4j
public class HandleExceptions {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleRuntimeExceptions(RuntimeException ex) {
        if (ex instanceof HttpClientErrorException)
            return handleClientErrorException((HttpClientErrorException) ex);
        else
            return handleOtherException(ex);
    }

    private ResponseEntity<Object> handleClientErrorException(HttpClientErrorException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    private ResponseEntity<Object> handleOtherException(RuntimeException ex) {
        log.error("Exception occurred, reason : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

}
