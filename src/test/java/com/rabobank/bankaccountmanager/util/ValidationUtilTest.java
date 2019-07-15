package com.rabobank.bankaccountmanager.util;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class ValidationUtilTest {

    @Test
    public void testNegative() {
        Assert.assertTrue("Not Negative", ValidationUtil.isNegative(BigDecimal.valueOf(-1)));
    }

    @Test
    public void testPositive() {
        Assert.assertFalse("Not Positive", ValidationUtil.isNegative(BigDecimal.TEN));
    }

}