package io.github.icon02.MMSPhotoEditBackend.controller;

import io.github.icon02.MMSPhotoEditBackend.exception.NoSessionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(NoSessionException.class)
    public ResponseEntity<NoSessionException> handleNoSessionException(NoSessionException e) {
        return ResponseEntity.status(401).body(e);
    }
}
