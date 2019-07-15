package com.rabobank.bankaccountmanager.controller;

import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.dto.AmountDto;
import com.rabobank.bankaccountmanager.exception.BankAccountManagerException;
import com.rabobank.bankaccountmanager.service.TransferService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class TransferControllerTest {

    @Mock
    private TransferService transferService;

    @InjectMocks
    private TransferController transferController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transferController)
                .setControllerAdvice(SBAControllerAdvice.class).build();
    }

    @Test
    public void testTransfer() throws Exception {
        Long fromBankAccount = 1L;
        Long toBankAccount = 2L;
        AmountDto amountDto = AmountDto.builder().amount(BigDecimal.ONE).build();

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_MULTI_PARAM_PATH,
                TransferController.SERVICE_PATH,
                fromBankAccount,
                toBankAccount);
        mockMvc.perform(post(
                servicePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataUtils.MAPPER.writeValueAsString(amountDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(transferService, Mockito.times(1))
                .transfer(fromBankAccount, toBankAccount, BigDecimal.ONE);

    }

    @Test
    public void testTransferBadRequest() throws Exception {
        Long fromBankAccount = 1L;
        Long toBankAccount = 2L;
        AmountDto amountDto = AmountDto.builder().amount(BigDecimal.ONE).build();

        Mockito.doThrow(BankAccountManagerException.to("testTransferBadRequest"))
                .when(transferService).transfer(fromBankAccount, toBankAccount, BigDecimal.ONE);

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_MULTI_PARAM_PATH,
                TransferController.SERVICE_PATH,
                fromBankAccount,
                toBankAccount);

        mockMvc.perform(post(
                servicePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataUtils.MAPPER.writeValueAsString(amountDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testTransferInternalError() throws Exception {
        Long fromBankAccount = 1L;
        Long toBankAccount = 2L;
        AmountDto amountDto = AmountDto.builder().amount(BigDecimal.ONE).build();

        Mockito.doThrow(new RuntimeException("testTransferBadRequest"))
                .when(transferService).transfer(fromBankAccount, toBankAccount, BigDecimal.ONE);

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_MULTI_PARAM_PATH,
                TransferController.SERVICE_PATH,
                fromBankAccount,
                toBankAccount);

        mockMvc.perform(post(
                servicePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataUtils.MAPPER.writeValueAsString(amountDto)))
                .andDo(print())
                .andExpect(status().isInternalServerError());

    }
}