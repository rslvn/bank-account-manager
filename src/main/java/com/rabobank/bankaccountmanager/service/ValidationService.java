package com.rabobank.bankaccountmanager.service;

import com.google.common.base.Preconditions;
import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import com.rabobank.bankaccountmanager.exception.InsufficientBalanceManagerException;
import com.rabobank.bankaccountmanager.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Validation service
 */
@Slf4j
@Service
public class ValidationService {

    public void checkWithdrawable(BankAccount bankAccount, BigDecimal amount) {
        if (ValidationUtil.isNegative(bankAccount.getCurrentBalance().subtract(amount))) {
            throw InsufficientBalanceManagerException.to(
                    "Account current balance is not available to withdraw. current balance: %s, amount: %s",
                    bankAccount.getCurrentBalance(),
                    amount);
        }
    }

    public void validateCurrentBalance(BankAccount bankAccount) {
        if (ValidationUtil.isNegative(bankAccount.getCurrentBalance())) {
            throw InsufficientBalanceManagerException.to(
                    "Account current balance is not available to withdraw/transfer. current balance: %s",
                    bankAccount.getCurrentBalance());
        }
    }

    public void validAmount(BigDecimal amount) {
        Preconditions.checkNotNull(amount, "amount can not be null");
        Preconditions.checkArgument(!ValidationUtil.isNegative(amount), "amount can not be negative");
    }
}
