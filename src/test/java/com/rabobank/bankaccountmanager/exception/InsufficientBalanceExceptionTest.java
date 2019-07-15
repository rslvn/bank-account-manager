package com.rabobank.bankaccountmanager.exception;

import org.junit.Test;

public class InsufficientBalanceExceptionTest {
    @Test(expected = InsufficientBalanceManagerException.class)
    public void textException() {
        throw InsufficientBalanceManagerException.to("textException");
    }
}