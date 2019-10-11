package com.rabobank.bankaccountmanager.domain.event;

import org.junit.Test;
import pl.pojo.tester.api.assertion.Assertions;
import pl.pojo.tester.api.assertion.Method;

public class TransactionHistorySaveEventTest {

    @Test
    public void tesTransactionHistorySaveEventAsPojo() {
        Assertions.assertPojoMethodsFor(TransactionHistorySaveEvent.class)
                .testing(Method.CONSTRUCTOR, Method.GETTER)
                .areWellImplemented();
    }

}