package com.rabobank.bankaccountmanager.converter;

import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.dto.BankAccountDto;
import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

public class BankAccountToDtoConverterTest {

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private BankAccountToDtoConverter bankAccountToDtoConverter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConvert() {
        BankAccount bankAccount = TestDataUtils.getBankAccount1();
        BankAccountDto bankAccountDto = bankAccountToDtoConverter.convert(bankAccount);
        Assert.assertNotNull(bankAccountDto);
        Assert.assertEquals(bankAccount.getCurrentBalance(), bankAccountDto.getBalance());
        Assert.assertEquals(bankAccount.getIban(), bankAccountDto.getIban());
    }
}