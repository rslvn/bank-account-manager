package com.rabobank.bankaccountmanager.repository;


import com.rabobank.bankaccountmanager.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


/**
 * customer of bank_account table
 */
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
