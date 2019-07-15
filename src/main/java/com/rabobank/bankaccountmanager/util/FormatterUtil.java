package com.rabobank.bankaccountmanager.util;

import com.rabobank.bankaccountmanager.domain.model.Customer;

public class FormatterUtil {

    private FormatterUtil() {
        // for sonarqube
    }

    private static final String FORMAT_CARD_HOLDER = "%s %s";

    public static String getCardHolderName(Customer customer) {
        return String.format(FORMAT_CARD_HOLDER, customer.getInitial(), customer.getLastName());
    }
}
