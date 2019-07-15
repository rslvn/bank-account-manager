package com.rabobank.bankaccountmanager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.dto.BalanceDto;
import com.rabobank.bankaccountmanager.domain.dto.BankAccountDto;
import com.rabobank.bankaccountmanager.domain.dto.CardDto;
import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import com.rabobank.bankaccountmanager.service.BankAccountService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class BankAccountControllerTest {

    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private BankAccountController bankAccountController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bankAccountController)
                .setControllerAdvice(SBAControllerAdvice.class)
                .build();
    }

    @Test
    public void testSaveCustomer() throws Exception {

        BankAccount bankAccount = TestDataUtils.getBankAccount1();

        BankAccountDto bankAccountDto = TestDataUtils.getBankAccountDto1();
        CardDto card = TestDataUtils.getDebitCardDto1();
        bankAccountDto.setCard(card);

        Mockito.when(conversionService.convert(bankAccountDto, BankAccount.class)).thenReturn(bankAccount);
        Mockito.when(bankAccountService
                .addBankAccount(
                        Mockito.eq(TestDataUtils.CUSTOMER_ID_FOR_NEW_BANK_ACCOUNT),
                        Mockito.any(BankAccount.class)))
                .thenReturn(bankAccount);


        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_PARAMETER_PATH,
                BankAccountController.SERVICE_PATH,
                TestDataUtils.CUSTOMER_ID_FOR_NEW_BANK_ACCOUNT);

        mockMvc
                .perform(put(servicePath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataUtils.MAPPER.writeValueAsString(bankAccountDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testSaveCustomerInternalError() throws Exception {

        BankAccountDto bankAccountDto = TestDataUtils.getBankAccountDto1();

        Mockito.when(conversionService.convert(bankAccountDto, BankAccount.class)).thenThrow(new NullPointerException("Dummy Exception"));

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_PARAMETER_PATH,
                BankAccountController.SERVICE_PATH,
                TestDataUtils.CUSTOMER_ID_FOR_NEW_BANK_ACCOUNT);

        mockMvc
                .perform(put(servicePath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataUtils.MAPPER.writeValueAsString(bankAccountDto)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testSaveCustomerBadRequest() throws Exception {

        BankAccountDto bankAccountDto = TestDataUtils.getBankAccountDtoNoCard();

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_PARAMETER_PATH,
                BankAccountController.SERVICE_PATH,
                TestDataUtils.CUSTOMER_ID_FOR_NEW_BANK_ACCOUNT);

        mockMvc
                .perform(put(servicePath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataUtils.MAPPER.writeValueAsString(bankAccountDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllBalance() throws Exception {
        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_METHOD_PATH,
                BankAccountController.SERVICE_PATH,
                BankAccountController.METHOD_GET_BALANCE_ALL);

        BalanceDto balance = TestDataUtils.getBalanceDto();
        BankAccount bankAccount = TestDataUtils.getBankAccount1();
        List<BankAccount> bankAccountList = Collections.singletonList(bankAccount);

        Mockito.when(conversionService.convert(Mockito.any(BankAccount.class), Mockito.eq(BalanceDto.class))).thenReturn(balance);
        Mockito.when(bankAccountService
                .getBankAccountList())
                .thenReturn(bankAccountList);

        MvcResult mvcResult = mockMvc
                .perform(get(servicePath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<BalanceDto> balanceDtoList = TestDataUtils.MAPPER.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<BalanceDto>>() {
        });

        Assert.assertFalse(balanceDtoList.isEmpty());
        balanceDtoList.forEach(balanceDto -> {
            Assert.assertNotNull(balanceDto.getBankAccountId());
            Assert.assertNotNull(balanceDto.getCurrentBalance());
        });
    }

    @Test
    public void testGetAllBalanceInternalError() throws Exception {
        BalanceDto balance = TestDataUtils.getBalanceDto();
        Mockito.when(bankAccountService.getBankAccountList()).thenThrow(new NullPointerException("Dummy Exception"));

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_METHOD_PATH,
                BankAccountController.SERVICE_PATH,
                BankAccountController.METHOD_GET_BALANCE_ALL);

        mockMvc
                .perform(get(servicePath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetBalance() throws Exception {


        Mockito.when(conversionService.convert(Mockito.any(BankAccount.class), Mockito.eq(BalanceDto.class)))
                .thenReturn(TestDataUtils.getBalanceDto());
        Mockito.when(bankAccountService.getBankAccount(TestDataUtils.BANK_ACCOUNT_DEBIT))
                .thenReturn(TestDataUtils.getBankAccount1());

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_METHOD_PARAM_PATH,
                BankAccountController.SERVICE_PATH,
                BankAccountController.METHOD_GET_BALANCE,
                TestDataUtils.BANK_ACCOUNT_DEBIT);

        // waits bad request
        MvcResult result = mockMvc.perform(get(servicePath)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()
                ).andReturn();

        BalanceDto balanceDto = TestDataUtils.MAPPER.readValue(result.getResponse().getContentAsString(), BalanceDto.class);

        Assert.assertNotNull(balanceDto);
        Assert.assertNotNull(balanceDto.getBankAccountId());
        Assert.assertNotNull(balanceDto.getCurrentBalance());
    }
}
