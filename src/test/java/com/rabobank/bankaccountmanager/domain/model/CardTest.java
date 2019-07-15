package com.rabobank.bankaccountmanager.domain.model;

import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.type.CardType;
import org.junit.Assert;
import org.junit.Test;
import pl.pojo.tester.api.assertion.Assertions;
import pl.pojo.tester.api.assertion.Method;

public class CardTest {

    @Test
    public void tesCardAsPojo() {
        Assertions.assertPojoMethodsFor(Card.class).testing(Method.values()).areWellImplemented();
    }

    @Test
    public void testCardBuilder() {
        Card card = TestDataUtils.getDebitCard1();

        Assert.assertEquals("card type mismatched", CardType.DEBIT_CARD, card.getCardType());
        Assert.assertEquals("card number mismatched", TestDataUtils.PASS_NUMBER1, card.getNumber());
    }

}