package com.rabobank.bankaccountmanager;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabobank.bankaccountmanager.domain.dto.BalanceDto;
import com.rabobank.bankaccountmanager.domain.dto.BankAccountDto;
import com.rabobank.bankaccountmanager.domain.dto.CardDto;
import com.rabobank.bankaccountmanager.domain.dto.CustomerDto;
import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import com.rabobank.bankaccountmanager.domain.model.Card;
import com.rabobank.bankaccountmanager.domain.model.Customer;
import com.rabobank.bankaccountmanager.domain.type.CardType;

import java.math.BigDecimal;

public class TestDataUtils {

    public static final String FORMAT_SERVICE_PATH = "/%s";
    public static final String FORMAT_SERVICE_METHOD_PATH = "/%s%s";
    public static final String FORMAT_SERVICE_PARAMETER_PATH = "/%s/%s";
    public static final String FORMAT_SERVICE_METHOD_PARAM_PATH = "/%s%s/%s";
    public static final String FORMAT_SERVICE_MULTI_PARAM_PATH = "/%s/%s/%s";

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static final Long ID_NOT_EXIST = 9999L;

    public static final Long CUSTOMER_ID_FOR_NEW_BANK_ACCOUNT = 1L;

    public static final Long BANK_ACCOUNT_DEBIT = 1L;
    public static final Long BANK_ACCOUNT_CREDIT = 2L;
    public static final Long BANK_ACCOUNT_DEBIT_NO_LIMIT = 3L;
    public static final Long BANK_ACCOUNT_CREADIT_NO_LIMIT = 4L;

    public static final String CREDIT_CARD_NUMBER1 = "1111222233334444";

    public static final String IBAN1 = "NL44RABO0123456789";
    public static final String IBAN2 = "NL44RABO1234567890";

    public static final String PASS_NUMBER1 = "0123";

    public static final String CVV1 = "123";

    public static final String EXPIRATION_DATE1 = "11/23";
    public static final String EXPIRATION_DATE2 = "12/23";

    public static final String FIRST_NAME1 = "First Name1";
    public static final String FIRST_NAME2 = "First Name2";

    public static final String LAST_NAME1 = "Last Name1";
    public static final String LAST_NAME2 = "Last Name2";

    public static final BigDecimal BALANCE1 = BigDecimal.valueOf(100);
    public static final BigDecimal BALANCE2 = BigDecimal.valueOf(50);

    public static final String INITIAL = "F.";


    public static final String MSG_ACCOUNT_NUMBER_MISMATCHED = "Account Number mismatched";


    public static Customer getCustomer1() {
        return Customer.builder()
                .firstName(FIRST_NAME1)
                .lastName(LAST_NAME1)
                .initial(INITIAL)
                .build();
    }

    public static Customer getCustomer2() {
        return Customer.builder()
                .firstName(FIRST_NAME2)
                .lastName(LAST_NAME2)
                .initial(INITIAL)
                .build();
    }

    public static CustomerDto getCustomerDto1() {
        return CustomerDto.builder()
                .firstName(FIRST_NAME1)
                .lastName(LAST_NAME1)
                .initial(INITIAL)
                .build();
    }

    public static CustomerDto getCustomerDto2() {
        return CustomerDto.builder()
                .firstName(FIRST_NAME2)
                .lastName(LAST_NAME2)
                .initial(INITIAL)
                .build();
    }

    public static BankAccount getBankAccount1() {
        return BankAccount.builder()
                .iban(IBAN1)
                .currentBalance(BALANCE1)
                .build();
    }

    public static BankAccount getBankAccount2() {
        return BankAccount.builder()
                .iban(IBAN2)
                .currentBalance(BALANCE2)
                .build();
    }

    public static Card getCreditCard1() {
        return Card.builder()
                .cardType(CardType.CREDIT_CARD)
                .number(CREDIT_CARD_NUMBER1)
                .holderName(INITIAL + " " + LAST_NAME1)
                .expiryDate(EXPIRATION_DATE1)
                .cvv(CVV1)
                .build();
    }

    public static Card getDebitCard1() {
        return Card.builder()
                .cardType(CardType.DEBIT_CARD)
                .number(PASS_NUMBER1)
                .holderName(INITIAL + " " + LAST_NAME2)
                .expiryDate(EXPIRATION_DATE2)
                .build();
    }

    public static CardDto getDebitCardDto1() {
        return CardDto.builder()
                .cardType(CardType.DEBIT_CARD)
                .number(PASS_NUMBER1)
                .expiryDate(EXPIRATION_DATE2)
                .build();
    }

    public static BankAccountDto getBankAccountDto1() {
        return BankAccountDto.builder()
                .iban(IBAN1)
                .balance(BALANCE1)
                .card(getDebitCardDto1())
                .build();
    }

    public static BankAccountDto getBankAccountDtoNoCard() {
        return BankAccountDto.builder()
                .iban(IBAN1)
                .balance(BALANCE1)
                .build();
    }

    public static BalanceDto getBalanceDto() {
        return BalanceDto.builder().bankAccountId(BANK_ACCOUNT_DEBIT).currentBalance(BALANCE1).build();
    }

}
