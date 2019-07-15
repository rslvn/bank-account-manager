package com.rabobank.bankaccountmanager.repository;

import com.rabobank.bankaccountmanager.domain.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * card of bank_account table
 */
@Transactional
public interface CardRepository extends JpaRepository<Card, Long> {
}
