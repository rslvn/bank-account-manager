package com.rabobank.bankaccountmanager.exception;

/**
 * The generic exception of the application
 */
public class BankAccountManagerException extends RuntimeException {

    protected BankAccountManagerException(String format, Object... parameters) {
        super(String.format(format, parameters));
    }

    protected BankAccountManagerException(Throwable cause, String format, Object... parameters) {
        super(String.format(format, parameters), cause);
    }

    public static BankAccountManagerException to(String format, Object... parameters) {
        return new BankAccountManagerException(format, parameters);
    }

    public static BankAccountManagerException to(Throwable cause, String format, Object... parameters) {
        return new BankAccountManagerException(cause, format, parameters);
    }

}
