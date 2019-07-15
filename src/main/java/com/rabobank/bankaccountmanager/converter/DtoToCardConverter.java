package com.rabobank.bankaccountmanager.converter;

import com.rabobank.bankaccountmanager.domain.dto.CardDto;
import com.rabobank.bankaccountmanager.domain.model.Card;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * CardDto to Card converter
 */
@Component
public class DtoToCardConverter implements Converter<CardDto, Card> {

    @Override
    public Card convert(CardDto cardDto) {

        return Card.builder()
                .cardType(cardDto.getCardType())
                .number(cardDto.getNumber())
                .expiryDate(cardDto.getExpiryDate())
                .cvv(cardDto.getCvv())
                .build();
    }

}
