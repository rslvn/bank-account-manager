package com.rabobank.bankaccountmanager.task;

import com.rabobank.bankaccountmanager.domain.model.TransactionHistory;
import com.rabobank.bankaccountmanager.exception.BankAccountManagerException;
import com.rabobank.bankaccountmanager.repository.TransactionHistoryRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Transaction history writer
 */
@Slf4j
@Builder
public class TransactionHistoryInserter implements Callable<String> {

    private TransactionHistoryRepository transactionHistoryRepository;
    private TransactionHistory fromTransactionHistory;
    private TransactionHistory toTransactionHistory;

    @Override
    public String call() {
        try {
            TransactionHistory savedFromTransactionHistory = transactionHistoryRepository.save(fromTransactionHistory);
            LOG.info("fromTransactionHistory is written. {}", savedFromTransactionHistory);
            Optional.ofNullable(toTransactionHistory).ifPresent(transactionHistory -> {
                // set correlationIds from savedFromTransactionHistory
                transactionHistory.setCorrelationId(savedFromTransactionHistory.getId());
                savedFromTransactionHistory.setCorrelationId(savedFromTransactionHistory.getId());

                // save toTransactionHistory
                TransactionHistory savedToTransactionHistory = transactionHistoryRepository.save(transactionHistory);
                LOG.info("toTransactionHistory is written. {}", savedToTransactionHistory);

                // update correlation Id
                transactionHistoryRepository.save(savedFromTransactionHistory);

            });
        } catch (Exception e) {
            LOG.error("", BankAccountManagerException.to(e, "Error while inserting TransactionHistory"));
        }
        return "OK";
    }
}
