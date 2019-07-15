package com.rabobank.bankaccountmanager.domain.model;

import com.rabobank.bankaccountmanager.TestDataUtils;
import org.junit.Assert;
import org.junit.Test;
import pl.pojo.tester.api.assertion.Assertions;
import pl.pojo.tester.api.assertion.Method;

public class CustomerTest {
    /**/
    @Test
    public void tesCustomerAsPojo() {
        Assertions.assertPojoMethodsFor(Card.class).testing(Method.values()).areWellImplemented();
    }

    @Test
    public void testCustomerBuilder() {
        Customer customer = TestDataUtils.getCustomer1();

        Assert.assertEquals("first name mismatched", TestDataUtils.FIRST_NAME1, customer.getFirstName());
        Assert.assertEquals("lastname mismatched", TestDataUtils.LAST_NAME1, customer.getLastName());
        Assert.assertEquals("initial mismatched", TestDataUtils.INITIAL, customer.getInitial());
    }

}