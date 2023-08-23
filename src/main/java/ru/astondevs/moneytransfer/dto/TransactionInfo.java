package ru.astondevs.moneytransfer.dto;

import ru.astondevs.moneytransfer.entities.TransactionsHistory;

public record TransactionInfo(
        String history
) {
    public static TransactionInfo of(TransactionsHistory transactionsHistory) {
        return new TransactionInfo(transactionsHistory.date() +
                transactionsHistory.dateTime() +
                transactionsHistory.event() +
                transactionsHistory.accountNumber() +
                transactionsHistory.ownerName() +
                transactionsHistory.accountOwner() +
                transactionsHistory.amountTransfer() +
                transactionsHistory.amount());
    }
}
