package com.rabobank.bankaccountmanager.domain.dto;

import org.junit.Assert;
import org.junit.Test;
import pl.pojo.tester.api.assertion.Assertions;
import pl.pojo.tester.api.assertion.Method;

import java.math.BigDecimal;

public class AmountDtoTest {

    @Test
    public void testAmountDtoAsPojo() {
        Assertions.assertPojoMethodsFor(AmountDto.class).testing(Method.values()).areWellImplemented();
    }

    @Test
    public void testAmountDtoBuilder() {
        AmountDto amountDto = AmountDto.builder().amount(BigDecimal.ONE).build();

        Assert.assertNotNull(amountDto);
        Assert.assertEquals(BigDecimal.ONE.doubleValue(), amountDto.getAmount().doubleValue(), 0.0);
    }
}