package com.rabobank.bankaccountmanager.controller;

import com.rabobank.bankaccountmanager.AbstractTestContainer;
import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.dto.AmountDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(initializers = {AbstractTestContainer.Initializer.class})
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class TransferControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testTransferFromDebitCard() throws Exception {
        Long fromBankAccount = TestDataUtils.BANK_ACCOUNT_DEBIT;
        Long toBankAccount = TestDataUtils.BANK_ACCOUNT_CREDIT;
        AmountDto amountDto = AmountDto.builder().amount(BigDecimal.ONE).build();

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_MULTI_PARAM_PATH,
                TransferController.SERVICE_PATH,
                fromBankAccount,
                toBankAccount);
        mvc.perform(post(
                servicePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataUtils.MAPPER.writeValueAsString(amountDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testTransferFromCreditCard() throws Exception {
        Long fromBankAccount = TestDataUtils.BANK_ACCOUNT_CREDIT;
        Long toBankAccount = TestDataUtils.BANK_ACCOUNT_DEBIT;
        AmountDto amountDto = AmountDto.builder().amount(BigDecimal.ONE).build();

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_MULTI_PARAM_PATH,
                TransferController.SERVICE_PATH,
                fromBankAccount,
                toBankAccount);
        mvc.perform(post(
                servicePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataUtils.MAPPER.writeValueAsString(amountDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testTransferFromDebitCardNoLimit() throws Exception {
        Long fromBankAccount = TestDataUtils.BANK_ACCOUNT_DEBIT_NO_LIMIT;
        Long toBankAccount = TestDataUtils.BANK_ACCOUNT_CREDIT;
        AmountDto amountDto = AmountDto.builder().amount(BigDecimal.TEN).build();

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_MULTI_PARAM_PATH,
                TransferController.SERVICE_PATH,
                fromBankAccount,
                toBankAccount);
        mvc.perform(post(
                servicePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataUtils.MAPPER.writeValueAsString(amountDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTransferFromCreditCardNoLimit() throws Exception {
        Long fromBankAccount = TestDataUtils.BANK_ACCOUNT_CREADIT_NO_LIMIT;
        Long toBankAccount = TestDataUtils.BANK_ACCOUNT_CREDIT;
        AmountDto amountDto = AmountDto.builder().amount(BigDecimal.TEN).build();

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_MULTI_PARAM_PATH,
                TransferController.SERVICE_PATH,
                fromBankAccount,
                toBankAccount);
        mvc.perform(post(
                servicePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataUtils.MAPPER.writeValueAsString(amountDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
