package ru.astondevs.moneytransfer.repositories;

import org.springframework.data.repository.ListCrudRepository;
import ru.astondevs.moneytransfer.entities.TransactionsHistory;

import java.util.List;

public interface HistoryRepository extends ListCrudRepository<TransactionsHistory, Long> {
    List<TransactionsHistory> findAllByAccountNumber(int accountNumber);
}
