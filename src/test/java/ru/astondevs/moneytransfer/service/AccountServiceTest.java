package ru.astondevs.moneytransfer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.astondevs.moneytransfer.dto.AccountInfo;
import ru.astondevs.moneytransfer.dto.DepositWithdraw;
import ru.astondevs.moneytransfer.dto.TransactionInfo;
import ru.astondevs.moneytransfer.dto.Transfer;
import ru.astondevs.moneytransfer.entities.Account;
import ru.astondevs.moneytransfer.entities.TransactionsHistory;
import ru.astondevs.moneytransfer.exceptions.AccountException;
import ru.astondevs.moneytransfer.exceptions.AccountNotFoundException;
import ru.astondevs.moneytransfer.logger.Logger;
import ru.astondevs.moneytransfer.repositories.AccountRepository;
import ru.astondevs.moneytransfer.repositories.HistoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private Logger logger;
    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccountWrongPinThrows() {
        assertThatThrownBy(() -> accountService.createAccount("ALEX", -1))
                .isInstanceOf(AccountException.class)
                .hasMessage("Пин код должен быть в пределах от 0 до 9999");
    }

    @Test
    void createAccountOk() {
        Account account = Account.of(1111, "ALEX", 111, 0);
        AccountInfo accountInfo = AccountInfo.of(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        assertThat(accountService.createAccount("ALEX", 111)).isNotNull();
        assertThat(accountService.createAccount("ALEX", 111)).isEqualTo(accountInfo);
    }

    @Test
    void getAllAccount() {
        Account account = Account.of(1111, "ALEX", 111, 0);
        List<AccountInfo> accountInfos = List.of(AccountInfo.of(account));
        when(accountRepository.findAll()).thenReturn(List.of(account));
        assertThat(accountService.getAllAccount()).isNotNull();
        assertThat(accountService.getAllAccount()).isEqualTo(accountInfos);
    }

    @Test
    void getTransactionsHistoryOfNull() {
        List<TransactionsHistory> transactionsHistory = new ArrayList<>();
        when(historyRepository.findAllByAccountNumber(1111)).thenReturn(transactionsHistory);
        assertThat(accountService.getTransactionsHistoryByAccountNumber(1111)).isNull();
    }

    @Test
    void getTransactionsHistoryOk() {
        TransactionsHistory history = TransactionsHistory.of("Event", 1111, "ALEX", "Transfer", 100.0);
        List<TransactionsHistory> transactionsHistory = List.of(history);
        List<TransactionInfo> transactionInfos = List.of(TransactionInfo.of(history));
        when(historyRepository.findAllByAccountNumber(1111)).thenReturn(transactionsHistory);
        assertThat(accountService.getTransactionsHistoryByAccountNumber(1111)).isNotNull();
        assertThat(accountService.getTransactionsHistoryByAccountNumber(1111)).isEqualTo(transactionInfos);
    }

    @Test
    void transferWrongNumberOfAccountToThrows() {
        Transfer transfer = new Transfer(111, 111, 100.0);
        when(accountRepository.findByAccountNumber(111)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> accountService.transfer(111, transfer))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessage("Номер счета получателя не найден.");
    }

    @Test
    void transferWrongNumberOfAccountFromThrows() {
        Transfer transfer = new Transfer(111, 1111, 100.0);
        Account account = Account.of(1111, "ALEX", 111, 100.0);
        when(accountRepository.findByAccountNumber(1111)).thenReturn(Optional.of(account));
        when(accountRepository.findByAccountNumber(2222)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> accountService.transfer(2222, transfer))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessage("Номер счета не найден.");

    }

    @Test
    void transferWrongPinNumberThrows() {
        Transfer transfer = new Transfer(333, 1111, 100.0);
        Account account = Account.of(1111, "ALEX", 111, 100.0);
        when(accountRepository.findByAccountNumber(1111)).thenReturn(Optional.of(account));
        assertThatThrownBy(() -> accountService.transfer(1111, transfer))
                .isInstanceOf(AccountException.class)
                .hasMessage("Неверный пин код.");
    }

    @Test
    void transferNotEnoughMoneyThrows() {
        Transfer transfer = new Transfer(111, 1111, 1000.0);
        Account account = Account.of(1111, "ALEX", 111, 100.0);
        when(accountRepository.findByAccountNumber(1111)).thenReturn(Optional.of(account));
        assertThatThrownBy(() -> accountService.transfer(1111, transfer))
                .isInstanceOf(AccountException.class)
                .hasMessage("Недостаточно средств на счете.");
    }

    @Test
    void transferWrongAmountThrows() {
        Transfer transfer = new Transfer(111, 1111, 0.0);
        Account account = Account.of(1111, "ALEX", 111, 100.0);
        when(accountRepository.findByAccountNumber(1111)).thenReturn(Optional.of(account));
        assertThatThrownBy(() -> accountService.transfer(1111, transfer))
                .isInstanceOf(AccountException.class)
                .hasMessage("Сумма должна быть больше 0.");
    }

    @Test
    void transferOk() {
        Transfer transfer = new Transfer(111, 1111, 100.0);
        Account account = Account.of(1111, "ALEX", 111, 100.0);
        when(accountRepository.findByAccountNumber(1111)).thenReturn(Optional.of(account));
        assertThat(accountService.transfer(1111, transfer))
                .contains("Операция выполнена успешно.");
        assertEquals(accountService.transfer(1111, transfer), "Операция выполнена успешно.");
    }

    @Test
    void depositWrongNumberOfAccountThrows() {
        DepositWithdraw deposit = new DepositWithdraw(111, 100.0);
        when(accountRepository.findByAccountNumber(1111)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> accountService.deposit(1111, deposit))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessage("Номер счета не найден.");
    }

    @Test
    void depositWrongPinNumberThrows() {
        DepositWithdraw deposit = new DepositWithdraw(222, 100.0);
        Account account = Account.of(1111, "ALEX", 111, 100.0);
        when(accountRepository.findByAccountNumber(1111)).thenReturn(Optional.of(account));
        assertThatThrownBy(() -> accountService.deposit(1111, deposit))
                .isInstanceOf(AccountException.class)
                .hasMessage("Неверный пин код.");
    }

    @Test
    void depositWrongAmountThrows() {
        DepositWithdraw deposit = new DepositWithdraw(111, -1);
        Account account = Account.of(1111, "ALEX", 111, 100.0);
        when(accountRepository.findByAccountNumber(1111)).thenReturn(Optional.of(account));
        assertThatThrownBy(() -> accountService.deposit(1111, deposit))
                .isInstanceOf(AccountException.class)
                .hasMessage("Сумма должна быть больше 0.");
    }

    @Test
    void depositOk() {
        DepositWithdraw deposit = new DepositWithdraw(111, 10.0);
        Account account = Account.of(1111, "ALEX", 111, 100.0);
        when(accountRepository.findByAccountNumber(1111)).thenReturn(Optional.of(account));
        assertThat(accountService.deposit(1111, deposit))
                .contains("Операция выполнена успешно.");
        assertEquals(accountService.deposit(1111, deposit), "Операция выполнена успешно.");
    }

    @Test
    void withdrawWrongNumberOfAccountThrows() {
        DepositWithdraw deposit = new DepositWithdraw(111, 100.0);
        when(accountRepository.findByAccountNumber(1111)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> accountService.withdraw(1111, deposit))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessage("Номер счета не найден.");
    }

    @Test
    void withdrawWrongPinNumberThrows() {
        DepositWithdraw deposit = new DepositWithdraw(222, 100.0);
        Account account = Account.of(1111, "ALEX", 111, 100.0);
        when(accountRepository.findByAccountNumber(1111)).thenReturn(Optional.of(account));
        assertThatThrownBy(() -> accountService.withdraw(1111, deposit))
                .isInstanceOf(AccountException.class)
                .hasMessage("Неверный пин код.");
    }

    @Test
    void withdrawNotEnoughMoneyThrows() {
        DepositWithdraw deposit = new DepositWithdraw(111, 1000.0);
        Account account = Account.of(1111, "ALEX", 111, 100.0);
        when(accountRepository.findByAccountNumber(1111)).thenReturn(Optional.of(account));
        assertThatThrownBy(() -> accountService.withdraw(1111, deposit))
                .isInstanceOf(AccountException.class)
                .hasMessage("Недостаточно средств на счете.");
    }

    @Test
    void withdrawWrongAmountThrows() {
        DepositWithdraw deposit = new DepositWithdraw(111, -1);
        Account account = Account.of(1111, "ALEX", 111, 100.0);
        when(accountRepository.findByAccountNumber(1111)).thenReturn(Optional.of(account));
        assertThatThrownBy(() -> accountService.withdraw(1111, deposit))
                .isInstanceOf(AccountException.class)
                .hasMessage("Сумма должна быть больше 0.");
    }

    @Test
    void withdrawOk() {
        DepositWithdraw deposit = new DepositWithdraw(111, 10.0);
        Account account = Account.of(1111, "ALEX", 111, 100.0);
        when(accountRepository.findByAccountNumber(1111)).thenReturn(Optional.of(account));
        assertThat(accountService.withdraw(1111, deposit))
                .contains("Операция выполнена успешно.");
        assertEquals(accountService.deposit(1111, deposit), "Операция выполнена успешно.");
    }
}
