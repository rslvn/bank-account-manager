package com.rabobank.bankaccountmanager.event;

import com.rabobank.bankaccountmanager.domain.event.TransactionHistorySaveEvent;
import com.rabobank.bankaccountmanager.domain.model.TransactionHistory;
import com.rabobank.bankaccountmanager.exception.BankAccountManagerException;
import com.rabobank.bankaccountmanager.repository.TransactionHistoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Component
public class SBAEventListener {

    private TransactionHistoryRepository transactionHistoryRepository;

    @Async
    @EventListener
    public void handleTransactionHistorySaveEvent(TransactionHistorySaveEvent transactionHistorySaveEvent) {
        try {
            TransactionHistory fromTransactionHistory = transactionHistorySaveEvent.getFromTransactionHistory();
            TransactionHistory toTransactionHistory = transactionHistorySaveEvent.getToTransactionHistory();

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
        } catch (BankAccountManagerException e) {
            LOG.error("", e);
        }
        catch (Exception e) {
            LOG.error("", BankAccountManagerException.to(e, "Error while inserting TransactionHistory"));
        }
    }
}
