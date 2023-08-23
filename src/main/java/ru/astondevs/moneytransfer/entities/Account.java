package ru.astondevs.moneytransfer.entities;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("accounts")
public record Account(
        @Id
        Long id,
        @NotNull(message = "Необходим номер счета.")
        int accountNumber,
        @NotNull(message = "Необходимо имя владельца счета.")
        String accountOwner,
        @NotNull(message = "Необходим пин код.")
        int pinNumber,
        double balance,
        @CreatedDate
        Instant createdDate
) {
    public static Account of(int accountNumber, String accountOwner, int pinNumber, double amount) {
        return new Account(null, accountNumber, accountOwner, pinNumber, amount, null);
    }
}
