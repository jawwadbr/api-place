package com.jawbr.challenge.exception.handler;

import com.jawbr.challenge.exception.errorResponse.PlaceErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PlaceExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<PlaceErrorResponse> handleException(Exception exc) {

        PlaceErrorResponse error = new PlaceErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
