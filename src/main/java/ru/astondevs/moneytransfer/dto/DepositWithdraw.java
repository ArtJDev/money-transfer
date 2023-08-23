package ru.astondevs.moneytransfer.dto;

public record DepositWithdraw(
        int pinNumber,
        double amount
) {
}
