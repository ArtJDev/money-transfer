package ru.astondevs.moneytransfer.dto;

import ru.astondevs.moneytransfer.entities.Account;

public record AccountInfo(
        int accountNumber,
        String accountOwner,
        double balance
) {
    public static AccountInfo of(Account account) {
        return new AccountInfo(account.accountNumber(), account.accountOwner(), account.balance());
    }
}
