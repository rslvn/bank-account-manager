package com.rabobank.bankaccountmanager.service;

import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import com.rabobank.bankaccountmanager.domain.type.TransactionType;
import com.rabobank.bankaccountmanager.exception.BankAccountManagerException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

public class TransactionFeeServiceTest {

    @InjectMocks
    private TransactionFeeService transactionFeeService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTotalAmount() {

        BigDecimal amount = BigDecimal.TEN;
        BigDecimal fee = BigDecimal.ONE;

        BigDecimal actual = transactionFeeService.getTotalAmount(amount, fee);


        Assert.assertEquals(BigDecimal.valueOf(11), actual);
    }

    @Test
    public void testDebitCardFee() {
        BankAccount bankAccount = TestDataUtils.getBankAccount1();
        bankAccount.setCard(TestDataUtils.getDebitCard1());
        BigDecimal amount = BigDecimal.TEN;
        BigDecimal actual = transactionFeeService.getFee(TransactionType.WITHDRAW, bankAccount, amount);

        Assert.assertEquals(BigDecimal.ZERO, actual);
    }

    @Test
    public void testCreditCardFeeWithdraw() {
        BankAccount bankAccount = BankAccount.builder()
                .currentBalance(BigDecimal.TEN)
                .card(TestDataUtils.getCreditCard1())
                .build();
        BigDecimal amount = BigDecimal.TEN;
        BigDecimal actual = transactionFeeService.getFee(TransactionType.WITHDRAW, bankAccount, amount);

        Assert.assertEquals(BigDecimal.valueOf(0.10).doubleValue(), actual.doubleValue(), 0.0);
    }

    @Test
    public void testCreditCardFeeTransfer() {
        BankAccount bankAccount = BankAccount.builder()
                .currentBalance(BigDecimal.TEN)
                .card(TestDataUtils.getCreditCard1())
                .build();
        BigDecimal amount = BigDecimal.TEN;
        BigDecimal actual = transactionFeeService.getFee(TransactionType.TRANSFER, bankAccount, amount);

        Assert.assertEquals(BigDecimal.valueOf(0.10).doubleValue(), actual.doubleValue(), 0.0);
    }

    @Test(expected = BankAccountManagerException.class)
    public void testFeeUnkownTransactionType() {
        BankAccount bankAccount = BankAccount.builder()
                .currentBalance(BigDecimal.TEN)
                .card(TestDataUtils.getCreditCard1())
                .build();
        BigDecimal amount = BigDecimal.TEN;
        transactionFeeService.getFee(null, bankAccount, amount);
    }

}