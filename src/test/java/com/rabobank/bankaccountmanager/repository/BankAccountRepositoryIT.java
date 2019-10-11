package com.rabobank.bankaccountmanager.repository;

import com.rabobank.bankaccountmanager.AbstractTestContainer;
import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import com.rabobank.bankaccountmanager.domain.model.Card;
import com.rabobank.bankaccountmanager.domain.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@ContextConfiguration(initializers = {AbstractTestContainer.Initializer.class})
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BankAccountRepositoryIT {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;


    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
//        cardRepository.deleteAll();
//        bankAccountRepository.deleteAll();
//        customerRepository.deleteAll();
    }

    @Transactional
    @Test
    public void card() {
        customerRepository.findAll().forEach(customer -> LOG.info("customer: {}", customer));

        Customer customer = TestDataUtils.getCustomer1();
        BankAccount bankAccount = TestDataUtils.getBankAccount1();
        Card card = TestDataUtils.getDebitCard1();
        card.setBankAccount(bankAccount);

        Customer savedCustomer = customerRepository.save(customer);

        customerRepository.findAll()
                .forEach(bankAccount1 -> LOG.info("bankAccount1: {}", bankAccount1));

        bankAccount.setCustomer(savedCustomer);

        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);

        bankAccountRepository.findAll()
                .forEach(bankAccount1 -> LOG.info("bankAccount1: {}", bankAccount1));

        Card savedCard = cardRepository.save(card);
        LOG.info("savedCard: {}", savedCard);

        int updatedRows = bankAccountRepository
                .decreaseCurrentBalance(savedBankAccount.getId(), BigDecimal.valueOf(45));
        LOG.info("UPDATED ROWS:  {}", updatedRows);

        bankAccountRepository
                .findById(savedBankAccount.getId())
                .ifPresent(bankAccount1 -> LOG.info("bankAccount1: {}", bankAccount1));


        updatedRows = bankAccountRepository
                .increaseCurrentBalance(savedBankAccount.getId(), BigDecimal.valueOf(100));
        LOG.info("UPDATED ROWS:  {}", updatedRows);

        bankAccountRepository
                .findById(savedBankAccount.getId())
                .ifPresent(bankAccount1 -> LOG.info("bankAccount1: {}", bankAccount1));

        savedBankAccount.setCurrentBalance(BigDecimal.valueOf(10000));

        savedBankAccount = bankAccountRepository.save(savedBankAccount);
        LOG.info("savedBankAccount:  {}", savedBankAccount);
    }
}
