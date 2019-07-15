package com.rabobank.bankaccountmanager.service;

import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import com.rabobank.bankaccountmanager.exception.InsufficientBalanceManagerException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

public class ValidationServiceTest {

    @InjectMocks
    private ValidationService validationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testCheckWithdrawable() {
        BankAccount bankAccount = BankAccount.builder().currentBalance(BigDecimal.TEN).build();

        // No exception means success
        validationService.checkWithdrawable(bankAccount, BigDecimal.ONE);
    }

    @Test(expected = InsufficientBalanceManagerException.class)
    public void testCheckWithdrawableNot() {
        BankAccount bankAccount = BankAccount.builder().currentBalance(BigDecimal.ONE).build();

        validationService.checkWithdrawable(bankAccount, BigDecimal.TEN);
    }

    @Test
    public void testValidateCurrentBalance() {
        BankAccount bankAccount = BankAccount.builder().currentBalance(BigDecimal.ONE).build();
        // No exception means success
        validationService.validateCurrentBalance(bankAccount);
    }

    @Test(expected = InsufficientBalanceManagerException.class)
    public void testValidateCurrentBalanceNegative() {
        BankAccount bankAccount = BankAccount.builder().currentBalance(BigDecimal.valueOf(-1)).build();

        validationService.validateCurrentBalance(bankAccount);
    }

}