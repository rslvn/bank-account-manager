package com.rabobank.bankaccountmanager.domain.dto;

import org.junit.Assert;
import org.junit.Test;
import pl.pojo.tester.api.assertion.Assertions;
import pl.pojo.tester.api.assertion.Method;

import java.math.BigDecimal;

public class BalanceDtoTest {
    @Test
    public void testBalanceDtoAsPojo() {
        Assertions.assertPojoMethodsFor(BalanceDto.class).testing(Method.values()).areWellImplemented();
    }

    @Test
    public void testAmountDtoBuilder() {
        BalanceDto balanceDto = BalanceDto.builder().bankAccountId(1L).currentBalance(BigDecimal.ONE).build();

        Assert.assertNotNull(balanceDto);
        Assert.assertEquals(1, balanceDto.getBankAccountId().longValue());
        Assert.assertEquals(BigDecimal.ONE.doubleValue(), balanceDto.getCurrentBalance().doubleValue(), 0.0);
    }
}