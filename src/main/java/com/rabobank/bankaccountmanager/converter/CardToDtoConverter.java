package com.rabobank.bankaccountmanager.converter;

import com.rabobank.bankaccountmanager.domain.dto.CardDto;
import com.rabobank.bankaccountmanager.domain.model.Card;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Card to CardDto converter
 */
@Component
public class CardToDtoConverter implements Converter<Card, CardDto> {

    @Override
    public CardDto convert(Card card) {

        return CardDto.builder()
                .cardType(card.getCardType())
                .number(card.getNumber())
                .expiryDate(card.getExpiryDate())
                .cvv(card.getCvv())
                .build();
    }

}
