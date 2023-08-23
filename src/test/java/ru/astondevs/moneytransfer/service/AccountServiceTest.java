package ru.astondevs.moneytransfer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.astondevs.moneytransfer.entities.Account;
import ru.astondevs.moneytransfer.exceptions.AccountException;
import ru.astondevs.moneytransfer.repository.AccountRepository;
import ru.astondevs.moneytransfer.repository.HistoryRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private HistoryRepository historyRepository;
    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccountWrongPinThrows() {
        assertThatThrownBy(() -> accountService.createAccount("ALEX", -1))
                .isInstanceOf(AccountException.class)
                .hasMessage("Пин код должен быть в пределах от 0 до 9999");
    }
}
