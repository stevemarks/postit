package uk.co.nominet.stevemarks.postit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerController {

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unknown error")
    @ExceptionHandler(Exception.class)
    public void unknownError() {
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unknown error")
    @ExceptionHandler(BadCredentialsException.class)
    public void badCredentialsException() {
    }
}