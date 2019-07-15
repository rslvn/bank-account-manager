package com.rabobank.bankaccountmanager.service;

import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Withdraw process management service
 */
@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class WithdrawService {

    private TransactionService transactionService;
    private BankAccountService bankAccountService;

    public void withdraw(Long bankAccountId, BigDecimal amount) {
        BankAccount bankAccount = bankAccountService.getBankAccount(bankAccountId);

        transactionService.executeWithdraw(bankAccount, amount);
    }

}
