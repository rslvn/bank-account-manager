package com.rabobank.bankaccountmanager.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * This class is an integration class for rest services
 */
@Builder
@Data
// we should add these two annotations if we use builder for DTOs
// Fixing the errors: no Creators, like default construct, exist
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDto {

    private String iban;
    private BigDecimal balance;
    private CardDto card;

}
