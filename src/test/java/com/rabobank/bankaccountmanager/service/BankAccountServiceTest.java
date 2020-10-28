package com.rabobank.bankaccountmanager.service;

import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import com.rabobank.bankaccountmanager.exception.BankAccountManagerException;
import com.rabobank.bankaccountmanager.repository.BankAccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.verify;

public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testIncreaseCurrentBalance() {
        BankAccount bankAccount = BankAccount.builder().id(1L).currentBalance(BigDecimal.ONE).build();

        Mockito.when(bankAccountRepository.increaseCurrentBalance(bankAccount.getId(), BigDecimal.ONE))
                .thenReturn(1);
        Mockito.when(bankAccountRepository.findById(bankAccount.getId()))
                .thenReturn(Optional.of(bankAccount));

        bankAccountService.increaseCurrentBalance(bankAccount, BigDecimal.ONE);

        verify(bankAccountRepository).increaseCurrentBalance(bankAccount.getId(), BigDecimal.ONE);
    }

    @Test(expected = BankAccountManagerException.class)
    public void testIncreaseCurrentBalanceNoBankAccount() {
        BankAccount bankAccount = BankAccount.builder().id(1L).currentBalance(BigDecimal.TEN).build();

        Mockito.when(bankAccountRepository.increaseCurrentBalance(bankAccount.getId(), BigDecimal.ONE))
                .thenReturn(1);

        bankAccountService.increaseCurrentBalance(bankAccount, BigDecimal.ONE);

        verify(bankAccountRepository).increaseCurrentBalance(bankAccount.getId(), BigDecimal.ONE);
    }

    @Test(expected = BankAccountManagerException.class)
    public void testIncreaseCurrentBalanceNotEffected() {
        BankAccount bankAccount = BankAccount.builder().id(1L).currentBalance(BigDecimal.TEN).build();
        bankAccountService.increaseCurrentBalance(bankAccount, BigDecimal.ONE);
    }

    @Test
    public void testDecreaseCurrentBalance() {
        BankAccount bankAccount = BankAccount.builder().id(1L).currentBalance(BigDecimal.TEN).build();

        Mockito.when(bankAccountRepository.decreaseCurrentBalance(bankAccount.getId(), BigDecimal.ONE))
                .thenReturn(1);
        Mockito.when(bankAccountRepository.findById(bankAccount.getId()))
                .thenReturn(Optional.of(bankAccount));

        bankAccountService.decreaseCurrentBalance(bankAccount, BigDecimal.ONE);

        verify(bankAccountRepository).decreaseCurrentBalance(bankAccount.getId(), BigDecimal.ONE);
    }

    @Test(expected = BankAccountManagerException.class)
    public void testDecreaseCurrentBalanceNoBankAccount() {
        BankAccount bankAccount = BankAccount.builder().id(1L).currentBalance(BigDecimal.TEN).build();

        Mockito.when(bankAccountRepository.decreaseCurrentBalance(bankAccount.getId(), BigDecimal.ONE))
                .thenReturn(1);

        bankAccountService.decreaseCurrentBalance(bankAccount, BigDecimal.ONE);
    }

    @Test(expected = BankAccountManagerException.class)
    public void testDecreaseCurrentBalanceNotEffected() {
        BankAccount bankAccount = BankAccount.builder().id(1L).currentBalance(BigDecimal.TEN).build();

        bankAccountService.decreaseCurrentBalance(bankAccount, BigDecimal.ONE);
    }
}
