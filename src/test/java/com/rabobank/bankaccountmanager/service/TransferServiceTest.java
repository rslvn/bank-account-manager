package com.rabobank.bankaccountmanager.service;

import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import com.rabobank.bankaccountmanager.exception.BankAccountManagerException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

public class TransferServiceTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private TransferService transferService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testWithdraw() {
        Mockito.when(bankAccountService.getBankAccount(1L))
                .thenReturn(TestDataUtils.getBankAccount1());
        Mockito.when(bankAccountService.getBankAccount(2L))
                .thenReturn(TestDataUtils.getBankAccount2());

        transferService.transfer(1L, 2L, BigDecimal.ONE);

        Mockito.verify(transactionService, Mockito.times(1))
                .executeTransfer(Mockito.any(BankAccount.class), Mockito.any(BankAccount.class), Mockito.eq(BigDecimal.ONE));
    }

    @Test(expected = RuntimeException.class)
    public void testWithdrawRuntimeException() {

        Mockito.when(bankAccountService.getBankAccount(1L)).thenThrow(new RuntimeException());

        transferService.transfer(1L, 2L, BigDecimal.ONE);
    }

    @Test(expected = BankAccountManagerException.class)
    public void testWithdrawRuntimeExceptionSBAException() {

        Mockito.when(bankAccountService.getBankAccount(1L)).thenThrow(BankAccountManagerException.to("No bank account"));

        transferService.transfer(1L, 2L, BigDecimal.ONE);
    }

}