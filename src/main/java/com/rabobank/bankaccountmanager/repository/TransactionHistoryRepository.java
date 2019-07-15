package com.rabobank.bankaccountmanager.repository;

import com.rabobank.bankaccountmanager.domain.model.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


/**
 * transaction_history of bank_account table
 */
@Transactional
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
}
