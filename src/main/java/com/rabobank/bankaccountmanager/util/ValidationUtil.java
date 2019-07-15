package com.rabobank.bankaccountmanager.util;

import java.math.BigDecimal;

public class ValidationUtil {

    private ValidationUtil() {
        // sor sonarqube
    }

    public static boolean isNegative(BigDecimal amount) {
        return BigDecimal.ZERO.compareTo(amount) > 0;
    }

}
