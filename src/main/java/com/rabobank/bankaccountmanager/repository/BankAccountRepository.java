package com.rabobank.bankaccountmanager.repository;

import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * repository of bank_account table
 */
@Transactional
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update BankAccount b set b.currentBalance = b.currentBalance - :amount where b.id = :bankAccountId")
    int decreaseCurrentBalance(@Param("bankAccountId") Long bankAccountId, @Param("amount") BigDecimal amount);

    @Modifying(clearAutomatically = true)
    @Query("update BankAccount b set b.currentBalance = b.currentBalance + :amount where b.id = :bankAccountId")
    int increaseCurrentBalance(@Param("bankAccountId") Long bankAccountId, @Param("amount") BigDecimal amount);
}
