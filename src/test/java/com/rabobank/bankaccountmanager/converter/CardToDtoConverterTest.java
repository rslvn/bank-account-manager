package com.rabobank.bankaccountmanager.converter;

import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.dto.CardDto;
import com.rabobank.bankaccountmanager.domain.model.Card;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class CardToDtoConverterTest {

    @InjectMocks
    private CardToDtoConverter cardToDtoConverter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConvert() {
        Card card = TestDataUtils.getDebitCard1();
        CardDto cardDto = cardToDtoConverter.convert(card);
        Assert.assertNotNull(cardDto);
        Assert.assertEquals(card.getCardType(), cardDto.getCardType());
        Assert.assertEquals(card.getExpiryDate(), cardDto.getExpiryDate());
        Assert.assertEquals(card.getNumber(), cardDto.getNumber());
        Assert.assertEquals(card.getCvv(), cardDto.getCvv());
    }
}