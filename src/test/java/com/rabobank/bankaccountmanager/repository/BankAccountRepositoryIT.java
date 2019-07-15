package com.rabobank.bankaccountmanager.repository;

import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import com.rabobank.bankaccountmanager.domain.model.Card;
import com.rabobank.bankaccountmanager.domain.model.Customer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;

@ContextConfiguration(initializers = {BankAccountRepositoryIT.Initializer.class})
@RunWith(SpringRunner.class)
@SpringBootTest
public class BankAccountRepositoryIT {

    @ClassRule
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres");

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


        customerRepository.findAll().forEach(customer -> System.out.println(customer));

        Customer customer = TestDataUtils.getCustomer1();
        BankAccount bankAccount = TestDataUtils.getBankAccount1();
//        bankAccount.setCard(TestDataUtils.getDebitCard1());
        Card card = TestDataUtils.getDebitCard1();
        card.setBankAccount(bankAccount);

        Customer savedCustomer = customerRepository.save(customer);

        customerRepository.findAll().forEach(bankAccount1 -> System.out.println(bankAccount1));


        bankAccount.setCustomer(savedCustomer);

        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);

        bankAccountRepository.findAll().forEach(bankAccount1 -> System.out.println(bankAccount1));


        Card savedCard = cardRepository.save(card);
        System.out.println(savedCard);


        int updatedRows = bankAccountRepository.decreaseCurrentBalance(savedBankAccount.getId(), BigDecimal.valueOf(45));

        System.out.println("UPDATED ROWS: " + updatedRows);
        bankAccountRepository.findById(savedBankAccount.getId()).ifPresent(bankAccount1 -> System.out.println(bankAccount1));


        updatedRows = bankAccountRepository.increaseCurrentBalance(savedBankAccount.getId(), BigDecimal.valueOf(100));
        System.out.println("UPDATED ROWS: " + updatedRows);
        bankAccountRepository.findById(savedBankAccount.getId()).ifPresent(bankAccount1 -> System.out.println(bankAccount1));

        savedBankAccount.setCurrentBalance(BigDecimal.valueOf(10000));

        savedBankAccount = bankAccountRepository.save(savedBankAccount);
        System.out.println(savedBankAccount);
    }


    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

}
