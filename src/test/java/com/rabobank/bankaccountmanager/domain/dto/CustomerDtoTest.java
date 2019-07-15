package com.rabobank.bankaccountmanager.domain.dto;

import com.rabobank.bankaccountmanager.TestDataUtils;
import org.junit.Assert;
import org.junit.Test;
import pl.pojo.tester.api.assertion.Assertions;
import pl.pojo.tester.api.assertion.Method;

public class CustomerDtoTest {

    @Test
    public void testCustomerDtoAsPojo() {
        Assertions.assertPojoMethodsFor(CustomerDto.class).testing(Method.values()).areWellImplemented();
    }

    @Test
    public void testCustomerDtoBuilder() {
        CustomerDto customerDto = TestDataUtils.getCustomerDto1();

        Assert.assertEquals("first name mismatched", TestDataUtils.FIRST_NAME1, customerDto.getFirstName());
        Assert.assertEquals("last name mismatched", TestDataUtils.LAST_NAME1, customerDto.getLastName());
        Assert.assertEquals("initial mismatched", TestDataUtils.INITIAL, customerDto.getInitial());
    }

}