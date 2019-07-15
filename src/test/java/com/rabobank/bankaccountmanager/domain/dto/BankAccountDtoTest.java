package com.rabobank.bankaccountmanager.domain.dto;

import org.junit.Assert;
import org.junit.Test;
import pl.pojo.tester.api.assertion.Assertions;
import pl.pojo.tester.api.assertion.Method;

import java.math.BigDecimal;

public class BankAccountDtoTest {

    @Test
    public void testBankAccountDtoAsPojo() {
        Assertions.assertPojoMethodsFor(BankAccountDto.class).testing(Method.values()).areWellImplemented();
    }

    @Test
    public void testBankAccountDtoBuilder() {
        String iban = "dummy card Number";
        BankAccountDto bankAccountDto = BankAccountDto.builder()
                .iban(iban)
                .balance(BigDecimal.TEN)
                .build();

        Assert.assertEquals("iban mismatched", iban, bankAccountDto.getIban());
        Assert.assertEquals("balance mismatched", BigDecimal.TEN, bankAccountDto.getBalance());
    }
}
