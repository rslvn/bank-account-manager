package com.rabobank.bankaccountmanager;

import com.rabobank.bankaccountmanager.service.CustomerService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(initializers = {AbstractTestContainer.Initializer.class})
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = {"test", "dev"})
public class BankAccountManagerApplicationTests {

    @Autowired
    private CustomerService customerService;

    @Test
    public void contextLoads() {
        Assert.assertNotNull(customerService);
    }

}
