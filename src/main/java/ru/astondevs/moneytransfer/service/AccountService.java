package ru.astondevs.moneytransfer.service;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.astondevs.moneytransfer.dto.AccountInfo;
import ru.astondevs.moneytransfer.dto.DepositWithdraw;
import ru.astondevs.moneytransfer.dto.TransactionInfo;
import ru.astondevs.moneytransfer.dto.Transfer;
import ru.astondevs.moneytransfer.entities.Account;
import ru.astondevs.moneytransfer.entities.TransactionsHistory;
import ru.astondevs.moneytransfer.exceptions.AccountNotFoundException;
import ru.astondevs.moneytransfer.exceptions.AccountException;
import ru.astondevs.moneytransfer.logger.Logger;
import ru.astondevs.moneytransfer.repository.AccountRepository;
import ru.astondevs.moneytransfer.repository.HistoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final HistoryRepository historyRepository;
    private final Logger logger;

    public AccountService(AccountRepository accountRepository, HistoryRepository historyRepository, Logger logger) {
        this.accountRepository = accountRepository;
        this.historyRepository = historyRepository;
        this.logger = logger;
    }

    public AccountInfo createAccount(String accountOwner, int pinNumber) {
        if (pinNumber < 0 || pinNumber > 9999) {
            throw new AccountException("Пин код должен быть в пределах от 0 до 9999");
        }
        int accountNumber = RandomUtils.nextInt(100_000, 1_099_999);
        Account account = accountRepository.save(Account.of(accountNumber, accountOwner, pinNumber, 0.0));
        logger.log(TransactionsHistory.of(", создан счет номер ", accountNumber, account.accountOwner(),
                ", баланс ", account.balance()));
        return AccountInfo.of(account);
    }

    public List<AccountInfo> getAllAccount() {
        List<AccountInfo> accountInfos = new ArrayList<>();
        accountRepository.findAll().forEach(account -> accountInfos.add(AccountInfo.of(account)));
        return accountInfos;
    }

    public List<TransactionInfo> getTransactionsHistoryByAccountNumber(int accountNumber) {
        List<TransactionsHistory> transactionsHistory = historyRepository.findAllByAccountNumber(accountNumber);
        if (!transactionsHistory.isEmpty()) {
            List<TransactionInfo> transactionInfos = new ArrayList<>();
            transactionsHistory.forEach(history -> transactionInfos.add(TransactionInfo.of(history)));
            return transactionInfos;
        }
        return null;
    }

    public String transfer(int accountNumber, Transfer parameters) {
        Account accountTo = accountRepository.findByAccountNumber(parameters.accountToNumber())
                .orElseThrow(() -> new AccountNotFoundException("Номер счета получателя не найден."));
        Account accountFrom = accountTransferWithdrawCheck(accountNumber, parameters.pinNumber(), parameters.amount());
        double accountFromNewAmount = accountFrom.balance() - parameters.amount();
        double accountToNewAmount = accountTo.balance() + parameters.amount();
        accountRepository.changeAmount(accountFrom.accountNumber(), accountFromNewAmount);
        accountRepository.changeAmount(accountTo.accountNumber(), accountToNewAmount);
        logger.log(TransactionsHistory.of(", перевод со счета ", accountNumber, accountFrom.accountOwner(),
                ", сумма перевода ", parameters.amount()));
        logger.log(TransactionsHistory.of(", перевод на счет ", accountTo.accountNumber(), accountTo.accountOwner(),
                ", сумма перевода ", parameters.amount()));
        return "Операция выполнена успешно.";
    }

    public String deposit(int accountNumber, DepositWithdraw parameters) {
        Account account = accountDepositCheck(accountNumber, parameters.pinNumber(), parameters.amount());
        double accountNewAmount = account.balance() + parameters.amount();
        accountRepository.changeAmount(accountNumber, accountNewAmount);
        logger.log(TransactionsHistory.of(", пополнение счета ", accountNumber, account.accountOwner(),
                ", сумма пополнения ", parameters.amount()));
        return "Операция выполнена успешно.";
    }

    public String withdraw(int accountNumber, DepositWithdraw parameters) {
        Account account = accountTransferWithdrawCheck(accountNumber, parameters.pinNumber(), parameters.amount());
        double accountNewAmount = account.balance() - parameters.amount();
        accountRepository.changeAmount(accountNumber, accountNewAmount);
        logger.log(TransactionsHistory.of(", снятие со счета ", accountNumber, account.accountOwner(),
                ", сумма снятия ", parameters.amount()));
        return "Операция выполнена успешно.";
    }

    private Account accountTransferWithdrawCheck(int accountNumber, int pinNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Номер счета не найден."));
        if (account.pinNumber() != pinNumber) {
            throw new AccountException("Неверный пин код.");
        }
        if (amount > account.balance()) {
            throw new AccountException("Недостаточно средств на счете.");
        }
        if (amount <= 0) {
            throw new AccountException("Сумма должна быть больше 0.");
        }
        return account;
    }

    private Account accountDepositCheck(int accountNumber, int pinNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Номер счета не найден."));
        if (account.pinNumber() != pinNumber) {
            throw new AccountException("Неверный пин код.");
        }
        if (amount <= 0) {
            throw new AccountException("Сумма должна быть больше 0.");
        }
        return account;
    }
}
