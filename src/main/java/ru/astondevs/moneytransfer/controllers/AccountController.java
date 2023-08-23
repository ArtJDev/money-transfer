package ru.astondevs.moneytransfer.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.astondevs.moneytransfer.dto.AccountInfo;
import ru.astondevs.moneytransfer.dto.DepositWithdraw;
import ru.astondevs.moneytransfer.dto.TransactionInfo;
import ru.astondevs.moneytransfer.dto.Transfer;
import ru.astondevs.moneytransfer.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<AccountInfo> getAllAccount() {
        return accountService.getAllAccount();
    }

    @GetMapping("/transactions/{accountNumber}")
    public List<TransactionInfo> getTransactionsHistoryByAccountNumber(@PathVariable("accountNumber") int accountNumber) {
        return accountService.getTransactionsHistoryByAccountNumber(accountNumber);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountInfo createAccount(String accountOwner, int pinNumber) {
        return accountService.createAccount(accountOwner, pinNumber);
    }

    @PostMapping("/transfer/{accountNumber}")
    public String transferAmount(@PathVariable("accountNumber") int accountNumber, @RequestBody Transfer transfer) {
        return accountService.transfer(accountNumber, transfer);
    }

    @PostMapping("/deposit/{accountNumber}")
    public String depositAmount(@PathVariable("accountNumber") int accountNumber, @RequestBody DepositWithdraw depositWithdraw) {
        return accountService.deposit(accountNumber, depositWithdraw);
    }

    @PostMapping("/withdraw/{accountNumber}")
    public String withdrawAmount(@PathVariable("accountNumber") int accountNumber, DepositWithdraw depositWithdraw) {
        return accountService.withdraw(accountNumber, depositWithdraw);
    }
}
