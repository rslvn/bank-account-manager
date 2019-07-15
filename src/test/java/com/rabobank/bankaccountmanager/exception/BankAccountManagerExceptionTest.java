package com.rabobank.bankaccountmanager.exception;

import org.junit.Test;

public class BankAccountManagerExceptionTest {

    @Test(expected = BankAccountManagerException.class)
    public void textException() {
        throw BankAccountManagerException.to("textException");
    }

    @Test(expected = BankAccountManagerException.class)
    public void textExceptionCause() {
        throw BankAccountManagerException.to(new RuntimeException("textExceptionCause"),
                "textException %d",
                1);
    }
}