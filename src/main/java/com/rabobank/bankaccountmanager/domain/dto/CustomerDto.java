package com.rabobank.bankaccountmanager.domain.dto;

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
public class CustomerDto {

    private String firstName;
    private String lastName;
    private String initial;
    private String email;
    private String phoneNumber;

}
