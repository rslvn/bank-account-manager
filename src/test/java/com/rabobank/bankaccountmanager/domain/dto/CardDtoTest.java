package com.rabobank.bankaccountmanager.domain.dto;

import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.type.CardType;
import org.junit.Assert;
import org.junit.Test;
import pl.pojo.tester.api.assertion.Assertions;
import pl.pojo.tester.api.assertion.Method;

public class CardDtoTest {

    @Test
    public void testCardDtoAsPojo() {
        Assertions.assertPojoMethodsFor(CardDto.class).testing(Method.values()).areWellImplemented();
    }

    @Test
    public void testCardDtoBuilder() {
        CardDto cardDto = TestDataUtils.getDebitCardDto1();

        Assert.assertEquals("cardType mismatched", CardType.DEBIT_CARD, cardDto.getCardType());
    }
}
