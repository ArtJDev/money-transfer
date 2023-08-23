package ru.astondevs.moneytransfer.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.astondevs.moneytransfer.exceptions.AccountException;
import ru.astondevs.moneytransfer.exceptions.AccountNotFoundException;

@RestControllerAdvice
public class ControllersAdvice {

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundException(AccountNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(AccountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String userNotFoundException(AccountException ex) {
        return ex.getMessage();
    }
}
