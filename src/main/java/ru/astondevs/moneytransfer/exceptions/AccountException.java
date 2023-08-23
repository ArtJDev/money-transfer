package ru.astondevs.moneytransfer.exceptions;

public class AccountException extends RuntimeException {
    public AccountException(String message) {
        super(message);
    }
}
