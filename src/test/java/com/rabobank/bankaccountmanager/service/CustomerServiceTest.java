package com.rabobank.bankaccountmanager.service;

import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.model.Customer;
import com.rabobank.bankaccountmanager.repository.CustomerRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCustomerList() {
        Customer customer = TestDataUtils.getCustomer1();
        Mockito.when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));
        List<Customer> customerList = customerService.getCustomerList();

        Assert.assertEquals("Customer list size mismatched", 1, customerList.size());
        Assert.assertEquals("Customer first name mismatched", customer.getFirstName(), customerList.get(0).getFirstName());
    }
}