package com.rabobank.bankaccountmanager.util;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;

public class ValidationUtil {

    private ValidationUtil() {
        // sor sonarqube
    }

    public static boolean isNegative(BigDecimal amount) {
        return BigDecimal.ZERO.compareTo(amount) > 0;
    }

}
