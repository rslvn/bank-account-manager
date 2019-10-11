package com.rabobank.bankaccountmanager.event;

import com.rabobank.bankaccountmanager.domain.event.TransactionHistorySaveEvent;
import com.rabobank.bankaccountmanager.domain.model.TransactionHistory;
import com.rabobank.bankaccountmanager.exception.BankAccountManagerException;
import com.rabobank.bankaccountmanager.repository.TransactionHistoryRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class SBAEventListenerTest {

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @Mock
    private TransactionHistorySaveEvent transactionHistorySaveEvent;

    @Mock
    private TransactionHistory fromTransactionHistory;

    @Mock
    private TransactionHistory toTransactionHistory;

    @InjectMocks
    private SBAEventListener sbaEventListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleTransactionHistorySaveEventBAMException() {

        Mockito.when(transactionHistorySaveEvent.getFromTransactionHistory()).thenReturn(fromTransactionHistory);
        Mockito.when(transactionHistorySaveEvent.getToTransactionHistory()).thenReturn(toTransactionHistory);

        Mockito.when(transactionHistoryRepository.save(fromTransactionHistory))
                .thenThrow(BankAccountManagerException.to("testHandleTransactionHistorySaveEventBAMException error"));

        sbaEventListener.handleTransactionHistorySaveEvent(transactionHistorySaveEvent);

        Mockito.verify(transactionHistoryRepository, Mockito.times(1))
                .save(fromTransactionHistory);
    }

    @Test
    public void testHandleTransactionHistorySaveEventAnyException() {

        Mockito.when(transactionHistorySaveEvent.getFromTransactionHistory()).thenReturn(fromTransactionHistory);
        Mockito.when(transactionHistorySaveEvent.getToTransactionHistory()).thenReturn(toTransactionHistory);

        Mockito.when(transactionHistoryRepository.save(fromTransactionHistory))
                .thenThrow(new RuntimeException("testHandleTransactionHistorySaveEventAnyException error"));

        sbaEventListener.handleTransactionHistorySaveEvent(transactionHistorySaveEvent);

        Mockito.verify(transactionHistoryRepository, Mockito.times(1))
                .save(fromTransactionHistory);
    }
}