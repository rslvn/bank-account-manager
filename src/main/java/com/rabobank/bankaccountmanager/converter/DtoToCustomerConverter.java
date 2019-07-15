package com.rabobank.bankaccountmanager.converter;


import com.rabobank.bankaccountmanager.domain.dto.CustomerDto;
import com.rabobank.bankaccountmanager.domain.model.Customer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * CustomerDto to Customer converter
 */
@Component
public class DtoToCustomerConverter implements Converter<CustomerDto, Customer> {

    @Override
    public Customer convert(CustomerDto customerDto) {

        return Customer.builder()
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .initial(customerDto.getInitial())
                .email(customerDto.getEmail())
                .phoneNumber(customerDto.getPhoneNumber())
                .build();
    }

}
