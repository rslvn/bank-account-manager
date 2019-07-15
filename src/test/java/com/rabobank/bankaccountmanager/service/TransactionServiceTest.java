package com.rabobank.bankaccountmanager.service;

import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import com.rabobank.bankaccountmanager.domain.type.TransactionType;
import com.rabobank.bankaccountmanager.exception.InsufficientBalanceManagerException;
import com.rabobank.bankaccountmanager.repository.TransactionHistoryRepository;
import com.rabobank.bankaccountmanager.task.TransactionHistoryInserter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;

public class TransactionServiceTest {

    @Mock
    private ValidationService validationService;
    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private TransactionFeeService transactionFeeService;
    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;
    @Mock
    private ExecutorService executorService;

    @InjectMocks
    private TransactionService transactionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testWithdraw() {

        BankAccount bankAccount = TestDataUtils.getBankAccount1();
        bankAccount.setCard(TestDataUtils.getDebitCard1());
        bankAccount.setCustomer(TestDataUtils.getCustomer1());

        Mockito.when(transactionFeeService.getFee(TransactionType.WITHDRAW, bankAccount, BigDecimal.ONE))
                .thenReturn(BigDecimal.ZERO);
        Mockito.when(transactionFeeService.getTotalAmount(BigDecimal.ONE, BigDecimal.ZERO))
                .thenReturn(BigDecimal.ONE);

        Mockito.when(bankAccountService.decreaseCurrentBalance(bankAccount, BigDecimal.ONE))
                .thenReturn(bankAccount);

        transactionService.executeWithdraw(bankAccount, BigDecimal.ONE);

        Mockito.verify(executorService, Mockito.times(1)).submit(Mockito.any(TransactionHistoryInserter.class));
    }

    @Test(expected = InsufficientBalanceManagerException.class)
    public void testWithdrawInsufficientBalanceException() {

        BankAccount bankAccount = TestDataUtils.getBankAccount1();
        bankAccount.setCard(TestDataUtils.getDebitCard1());
        bankAccount.setCustomer(TestDataUtils.getCustomer1());

        Mockito.when(transactionFeeService.getFee(TransactionType.WITHDRAW, bankAccount, BigDecimal.ONE))
                .thenReturn(BigDecimal.ZERO);
        Mockito.when(transactionFeeService.getTotalAmount(BigDecimal.ONE, BigDecimal.ZERO))
                .thenReturn(BigDecimal.ONE);

        Mockito.when(bankAccountService.decreaseCurrentBalance(bankAccount, BigDecimal.ONE))
                .thenReturn(bankAccount);

        Mockito.doThrow(InsufficientBalanceManagerException.to("")).when(validationService).validateCurrentBalance(bankAccount);

        transactionService.executeWithdraw(bankAccount, BigDecimal.ONE);

        Mockito.verify(executorService, Mockito.times(1)).submit(Mockito.any(TransactionHistoryInserter.class));
    }

    @Test(expected = RuntimeException.class)
    public void testWithdrawRuntimeException() {

        BankAccount bankAccount = TestDataUtils.getBankAccount1();
        bankAccount.setCard(TestDataUtils.getDebitCard1());
        bankAccount.setCustomer(TestDataUtils.getCustomer1());

        Mockito.when(transactionFeeService.getFee(TransactionType.WITHDRAW, bankAccount, BigDecimal.ONE))
                .thenReturn(BigDecimal.ZERO);
        Mockito.when(transactionFeeService.getTotalAmount(BigDecimal.ONE, BigDecimal.ZERO))
                .thenReturn(BigDecimal.ONE);

        Mockito.when(bankAccountService.decreaseCurrentBalance(bankAccount, BigDecimal.ONE))
                .thenReturn(bankAccount);

        Mockito.doThrow(new RuntimeException()).when(validationService).validateCurrentBalance(bankAccount);

        transactionService.executeWithdraw(bankAccount, BigDecimal.ONE);

        Mockito.verify(executorService, Mockito.times(1)).submit(Mockito.any(TransactionHistoryInserter.class));
    }

    @Test(expected = RuntimeException.class)
    public void testWithdrawErrorOnRecordingHistory() {

        BankAccount bankAccount = TestDataUtils.getBankAccount1();
        bankAccount.setCard(TestDataUtils.getDebitCard1());
        bankAccount.setCustomer(TestDataUtils.getCustomer1());

        Mockito.when(transactionFeeService.getFee(TransactionType.WITHDRAW, bankAccount, BigDecimal.ONE))
                .thenReturn(BigDecimal.ZERO);
        Mockito.when(transactionFeeService.getTotalAmount(BigDecimal.ONE, BigDecimal.ZERO))
                .thenReturn(BigDecimal.ONE);

        Mockito.when(bankAccountService.decreaseCurrentBalance(bankAccount, BigDecimal.ONE))
                .thenReturn(bankAccount);

        Mockito.doThrow(new RuntimeException()).when(validationService).validateCurrentBalance(bankAccount);
        Mockito.doThrow(new RuntimeException()).when(executorService).submit(Mockito.any(TransactionHistoryInserter.class));

        transactionService.executeWithdraw(bankAccount, BigDecimal.ONE);

        Mockito.verify(executorService, Mockito.times(1)).submit(Mockito.any(TransactionHistoryInserter.class));
    }


    @Test
    public void testTransfer() {

        BankAccount fromBankAccount = TestDataUtils.getBankAccount1();
        fromBankAccount.setId(TestDataUtils.BANK_ACCOUNT_DEBIT);
        fromBankAccount.setCard(TestDataUtils.getDebitCard1());
        fromBankAccount.setCustomer(TestDataUtils.getCustomer1());

        BankAccount toBankAccount = TestDataUtils.getBankAccount1();
        toBankAccount.setId(TestDataUtils.BANK_ACCOUNT_CREADIT);
        toBankAccount.setCard(TestDataUtils.getDebitCard1());
        toBankAccount.setCustomer(TestDataUtils.getCustomer1());

        Mockito.when(transactionFeeService.getFee(TransactionType.WITHDRAW, fromBankAccount, BigDecimal.ONE))
                .thenReturn(BigDecimal.ZERO);
        Mockito.when(transactionFeeService.getTotalAmount(BigDecimal.ONE, BigDecimal.ZERO))
                .thenReturn(BigDecimal.ONE);

        Mockito.when(bankAccountService.decreaseCurrentBalance(fromBankAccount, BigDecimal.ONE))
                .thenReturn(fromBankAccount);

        Mockito.when(bankAccountService.increaseCurrentBalance(toBankAccount, BigDecimal.ONE))
                .thenReturn(toBankAccount);

        transactionService.executeTransfer(fromBankAccount, toBankAccount, BigDecimal.ONE);

        Mockito.verify(executorService, Mockito.times(1)).submit(Mockito.any(TransactionHistoryInserter.class));
    }

    @Test
    public void testTransferOnRecordingHistory() {

        BankAccount fromBankAccount = TestDataUtils.getBankAccount1();
        fromBankAccount.setId(TestDataUtils.BANK_ACCOUNT_DEBIT);
        fromBankAccount.setCard(TestDataUtils.getDebitCard1());
        fromBankAccount.setCustomer(TestDataUtils.getCustomer1());

        BankAccount toBankAccount = TestDataUtils.getBankAccount1();
        toBankAccount.setId(TestDataUtils.BANK_ACCOUNT_CREADIT);
        toBankAccount.setCard(TestDataUtils.getDebitCard1());
        toBankAccount.setCustomer(TestDataUtils.getCustomer1());

        Mockito.when(transactionFeeService.getFee(TransactionType.WITHDRAW, fromBankAccount, BigDecimal.ONE))
                .thenReturn(BigDecimal.ZERO);
        Mockito.when(transactionFeeService.getTotalAmount(BigDecimal.ONE, BigDecimal.ZERO))
                .thenReturn(BigDecimal.ONE);

        Mockito.when(bankAccountService.decreaseCurrentBalance(fromBankAccount, BigDecimal.ONE))
                .thenReturn(fromBankAccount);
        Mockito.when(bankAccountService.increaseCurrentBalance(toBankAccount, BigDecimal.ONE))
                .thenReturn(toBankAccount);

        Mockito.doThrow(new RuntimeException()).when(executorService).submit(Mockito.any(TransactionHistoryInserter.class));

        transactionService.executeTransfer(fromBankAccount, toBankAccount, BigDecimal.ONE);

        Mockito.verify(executorService, Mockito.times(1)).submit(Mockito.any(TransactionHistoryInserter.class));
    }

    @Test(expected = InsufficientBalanceManagerException.class)
    public void testTransferInsufficientBalanceException() {

        BankAccount fromBankAccount = TestDataUtils.getBankAccount1();
        fromBankAccount.setId(TestDataUtils.BANK_ACCOUNT_DEBIT);
        fromBankAccount.setCard(TestDataUtils.getDebitCard1());
        fromBankAccount.setCustomer(TestDataUtils.getCustomer1());

        BankAccount toBankAccount = TestDataUtils.getBankAccount1();
        toBankAccount.setId(TestDataUtils.BANK_ACCOUNT_CREADIT);
        toBankAccount.setCard(TestDataUtils.getDebitCard1());
        toBankAccount.setCustomer(TestDataUtils.getCustomer1());

        Mockito.when(transactionFeeService.getFee(TransactionType.WITHDRAW, fromBankAccount, BigDecimal.ONE))
                .thenReturn(BigDecimal.ZERO);
        Mockito.when(transactionFeeService.getTotalAmount(BigDecimal.ONE, BigDecimal.ZERO))
                .thenReturn(BigDecimal.ONE);

        Mockito.when(bankAccountService.decreaseCurrentBalance(fromBankAccount, BigDecimal.ONE))
                .thenReturn(fromBankAccount);
        Mockito.when(bankAccountService.increaseCurrentBalance(toBankAccount, BigDecimal.ONE))
                .thenThrow(InsufficientBalanceManagerException.to("putMoney"));

        transactionService.executeTransfer(fromBankAccount, toBankAccount, BigDecimal.ONE);

        Mockito.verify(executorService, Mockito.times(1)).submit(Mockito.any(TransactionHistoryInserter.class));
    }

    @Test(expected = RuntimeException.class)
    public void testTransferRuntimeException() {

        BankAccount fromBankAccount = TestDataUtils.getBankAccount1();
        fromBankAccount.setId(TestDataUtils.BANK_ACCOUNT_DEBIT);
        fromBankAccount.setCard(TestDataUtils.getDebitCard1());
        fromBankAccount.setCustomer(TestDataUtils.getCustomer1());

        BankAccount toBankAccount = TestDataUtils.getBankAccount1();
        toBankAccount.setId(TestDataUtils.BANK_ACCOUNT_CREADIT);
        toBankAccount.setCard(TestDataUtils.getDebitCard1());
        toBankAccount.setCustomer(TestDataUtils.getCustomer1());

        Mockito.when(transactionFeeService.getFee(TransactionType.WITHDRAW, fromBankAccount, BigDecimal.ONE))
                .thenReturn(BigDecimal.ZERO);
        Mockito.when(transactionFeeService.getTotalAmount(BigDecimal.ONE, BigDecimal.ZERO))
                .thenReturn(BigDecimal.ONE);

        Mockito.when(bankAccountService.decreaseCurrentBalance(fromBankAccount, BigDecimal.ONE))
                .thenReturn(fromBankAccount);
        Mockito.when(bankAccountService.increaseCurrentBalance(toBankAccount, BigDecimal.ONE))
                .thenThrow(new RuntimeException("put money runtime"));

        transactionService.executeTransfer(fromBankAccount, toBankAccount, BigDecimal.ONE);

        Mockito.verify(executorService, Mockito.times(1)).submit(Mockito.any(TransactionHistoryInserter.class));
    }
}