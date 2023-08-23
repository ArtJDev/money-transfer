package ru.astondevs.moneytransfer.logger;

import org.springframework.stereotype.Component;
import ru.astondevs.moneytransfer.entities.TransactionsHistory;
import ru.astondevs.moneytransfer.repository.HistoryRepository;

@Component
public class Logger {

    private final HistoryRepository historyRepository;

    public Logger(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public void log(TransactionsHistory transactionsHistory) {
        historyRepository.save(transactionsHistory);
    }
}
