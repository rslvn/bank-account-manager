package com.rabobank.bankaccountmanager.service;

import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

public class WithdrawServiceTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private WithdrawService withdrawService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWithdraw() {
        BankAccount bankAccount = TestDataUtils.getBankAccount1();
        Mockito.when(bankAccountService.getBankAccount(1L)).thenReturn(bankAccount);

        withdrawService.withdraw(1L, BigDecimal.ONE);

        Mockito.verify(transactionService, Mockito.times(1))
                .executeWithdraw(Mockito.any(BankAccount.class), Mockito.eq(BigDecimal.ONE));
    }

    @Test(expected = RuntimeException.class)
    public void testWithdrawRuntimeException() {

        Mockito.when(bankAccountService.getBankAccount(1L)).thenThrow(new RuntimeException());

        withdrawService.withdraw(1L, BigDecimal.ONE);
    }
}