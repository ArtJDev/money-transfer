package ru.astondevs.moneytransfer.dto;

public record Transfer(
        int pinNumber,
        int accountToNumber,
        double amount
) {
}
