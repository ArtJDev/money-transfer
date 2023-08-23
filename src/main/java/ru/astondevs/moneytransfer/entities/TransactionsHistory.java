package ru.astondevs.moneytransfer.entities;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("history")
public record TransactionsHistory(
        @Id
        Long id,
        String date,
        @CreatedDate
        Instant dateTime,
        String event,
        int accountNumber,
        String ownerName,
        String accountOwner,
        String amountTransfer,
        double amount
) {
    public static TransactionsHistory of(String event, int accountNumber, String accountOwner,
                                         String amountTransfer, double amount) {
        return new TransactionsHistory(null, "Дата: ", null, event, accountNumber,
                ", имя владельца ", accountOwner, amountTransfer, amount);
    }
}
