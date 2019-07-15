package com.rabobank.bankaccountmanager.domain.dto;

import com.rabobank.bankaccountmanager.domain.type.CardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is an integration class for rest services
 */
@Builder
@Data
// we should add these two annotations if we use builder for DTOs
// Fixing the errors: no Creators, like default construct, exist
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {

    private CardType cardType;
    private String number;
    private String expiryDate;
    private String cvv;
}
