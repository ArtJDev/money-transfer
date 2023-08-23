package ru.astondevs.moneytransfer.repositories;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import ru.astondevs.moneytransfer.entities.Account;

import java.util.Optional;

public interface AccountRepository extends ListCrudRepository<Account, Long> {

    Optional<Account> findByAccountNumber(int accountNumber);

    @Modifying
    @Query("UPDATE accounts SET balance = :amount WHERE account_number = :accountNumber")
    void changeAmount(long accountNumber, double amount);
}
