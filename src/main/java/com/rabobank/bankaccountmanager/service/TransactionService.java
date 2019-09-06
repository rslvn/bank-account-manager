package com.rabobank.bankaccountmanager.service;

import com.google.common.base.Preconditions;
import com.rabobank.bankaccountmanager.domain.event.TransactionHistorySaveEvent;
import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import com.rabobank.bankaccountmanager.domain.model.TransactionHistory;
import com.rabobank.bankaccountmanager.domain.type.StatementType;
import com.rabobank.bankaccountmanager.domain.type.TransactionStatus;
import com.rabobank.bankaccountmanager.domain.type.TransactionType;
import com.rabobank.bankaccountmanager.exception.InsufficientBalanceManagerException;
import com.rabobank.bankaccountmanager.repository.TransactionHistoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * Transaction management service as TRANSFER and WITHDRAW
 */
@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class TransactionService {

    private static final String ERROR_CREATING_INSERTER = "Error while transaction history to executor";

    private ValidationService validationService;
    private BankAccountService bankAccountService;
    private TransactionFeeService transactionFeeService;
    private TransactionHistoryRepository transactionHistoryRepository;
    private ExecutorService executorService;
    private ApplicationEventPublisher applicationEventPublisher;

    public void executeWithdraw(BankAccount bankAccount, BigDecimal amount) {
        // validate parameters
        Preconditions.checkNotNull(bankAccount, "bankAccount can not be null");
        validationService.validAmount(amount);

        TransactionHistory.TransactionHistoryBuilder transactionHistoryBuilder = getTransactionHistoryBuilder(
                TransactionType.WITHDRAW,
                StatementType.EXPENSE,
                bankAccount,
                amount);
        try {

            takeMoney(transactionHistoryBuilder, bankAccount, amount);

        } catch (InsufficientBalanceManagerException e) {
            setTransactionHistoryBuilderAsFail(transactionHistoryBuilder, TransactionStatus.INSUFFICIENT_BALANCE, e.getMessage());
            throw e;

        } catch (RuntimeException e) {
            setTransactionHistoryBuilderAsFail(transactionHistoryBuilder, TransactionStatus.FAIL, e.getMessage());
            throw e;

        } finally {
            sendTransactionHistorySaveEvent(transactionHistoryBuilder);
        }
    }


    public void executeTransfer(BankAccount fromBankAccount, BankAccount toBankAccount, final BigDecimal amount) {
        // validate parameters
        Preconditions.checkNotNull(fromBankAccount, "fromBbankAccount can not be null");
        Preconditions.checkNotNull(toBankAccount, "toBankAccount can not be null");
        Preconditions.checkArgument(!Objects.equals(fromBankAccount.getId(), toBankAccount.getId()),
                "Transfer can not executed an account to the same account. bankAccountId: ",
                fromBankAccount.getId());


        // create TransactionHistoryBuilder for fromBankAccount
        TransactionHistory.TransactionHistoryBuilder fromTransactionHistoryBuilder = getTransactionHistoryBuilder(
                TransactionType.TRANSFER,
                StatementType.EXPENSE,
                fromBankAccount,
                amount);

        // create TransactionHistoryBuilder for toBankAccount
        TransactionHistory.TransactionHistoryBuilder toTransactionHistoryBuilder = getTransactionHistoryBuilder(
                TransactionType.TRANSFER,
                StatementType.INCOME,
                toBankAccount,
                amount);

        try {
            validationService.validAmount(amount);

            takeMoney(fromTransactionHistoryBuilder, fromBankAccount, amount);
            putMoney(toTransactionHistoryBuilder, toBankAccount, amount);

        } catch (InsufficientBalanceManagerException e) {
            setTransactionHistoryBuilderAsFail(fromTransactionHistoryBuilder, TransactionStatus.INSUFFICIENT_BALANCE, e.getMessage());
            setTransactionHistoryBuilderAsFail(toTransactionHistoryBuilder, TransactionStatus.INSUFFICIENT_BALANCE, e.getMessage());
            throw e;

        } catch (RuntimeException e) {
            setTransactionHistoryBuilderAsFail(fromTransactionHistoryBuilder, TransactionStatus.FAIL, e.getMessage());
            setTransactionHistoryBuilderAsFail(toTransactionHistoryBuilder, TransactionStatus.FAIL, e.getMessage());
            throw e;

        } finally {
            sendTransactionHistorySaveEvent(fromTransactionHistoryBuilder, toTransactionHistoryBuilder);
        }
    }

    private void takeMoney(TransactionHistory.TransactionHistoryBuilder transactionHistoryBuilder, BankAccount bankAccount, BigDecimal amount) {
        BigDecimal fee = transactionFeeService.getFee(TransactionType.WITHDRAW, bankAccount, amount);
        BigDecimal totalAmount = transactionFeeService.getTotalAmount(amount, fee);

        validationService.checkWithdrawable(bankAccount, totalAmount);

        BankAccount updatedBankAccount = bankAccountService.decreaseCurrentBalance(bankAccount, totalAmount);
        validationService.validateCurrentBalance(updatedBankAccount);

        transactionHistoryBuilder.status(TransactionStatus.SUCCESS)
                .fee(fee)
                .totalAmount(totalAmount)
                .afterBalance(updatedBankAccount.getCurrentBalance());
    }

    private void putMoney(TransactionHistory.TransactionHistoryBuilder transactionHistoryBuilder, BankAccount bankAccount, BigDecimal amount) {

        BankAccount updatedBankAccount = bankAccountService.increaseCurrentBalance(bankAccount, amount);

        transactionHistoryBuilder.status(TransactionStatus.SUCCESS)
                .fee(BigDecimal.ZERO)
                .totalAmount(amount)
                .afterBalance(updatedBankAccount.getCurrentBalance());
    }

    private TransactionHistory.TransactionHistoryBuilder getTransactionHistoryBuilder(
            TransactionType transactionType,
            StatementType statementType,
            BankAccount bankAccount,
            BigDecimal amount) {

        return TransactionHistory.builder()
                .type(transactionType)
                .statementType(statementType)
                .amount(amount)
                .customerId(bankAccount.getCustomer().getId())
                .bankAccountId(bankAccount.getId())
                .cardId(bankAccount.getCard().getId())
                .beforeBalance(bankAccount.getCurrentBalance());
    }

    private void setTransactionHistoryBuilderAsFail(
            TransactionHistory.TransactionHistoryBuilder transactionHistoryBuilder,
            TransactionStatus transactionStatus,
            String failingReason) {

        transactionHistoryBuilder.status(transactionStatus)
                .failingReason(failingReason);
    }

    private void sendTransactionHistorySaveEvent(TransactionHistory.TransactionHistoryBuilder... transactionHistoryBuilder) {
        Arrays.asList(transactionHistoryBuilder).forEach(this::sendTransactionHistorySaveEvent);
    }

    private void sendTransactionHistorySaveEvent(TransactionHistory.TransactionHistoryBuilder transactionHistoryBuilder) {
        try {
            applicationEventPublisher.publishEvent(TransactionHistorySaveEvent.builder()
                    .fromTransactionHistory(transactionHistoryBuilder.build())
                    .eventSource(getClass().getName())
                    .build());
        } catch (Exception e) {
            LOG.error(ERROR_CREATING_INSERTER, e);
        }
    }

}
