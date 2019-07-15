package com.rabobank.bankaccountmanager.controller;

import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.dto.AmountDto;
import com.rabobank.bankaccountmanager.exception.BankAccountManagerException;
import com.rabobank.bankaccountmanager.service.WithdrawService;
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
public class WithdrawControllerTest {

    @Mock
    private WithdrawService withdrawService;

    @InjectMocks
    private WithdrawController withdrawController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(withdrawController)
                .setControllerAdvice(SBAControllerAdvice.class).build();
    }

    @Test
    public void testWithdraw() throws Exception {
        Long bankAccount = 1L;
        AmountDto amountDto = AmountDto.builder().amount(BigDecimal.ONE).build();

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_PARAMETER_PATH,
                WithdrawController.SERVICE_PATH,
                bankAccount);
        mockMvc.perform(post(
                servicePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataUtils.MAPPER.writeValueAsString(amountDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(withdrawService, Mockito.times(1)).withdraw(bankAccount, BigDecimal.ONE);
    }

    @Test
    public void testWithdrawBadRequest() throws Exception {
        Long bankAccount = 1L;
        AmountDto amountDto = AmountDto.builder().amount(BigDecimal.ONE).build();

        Mockito.doThrow(BankAccountManagerException.to("testWithdrawBadRequest"))
                .when(withdrawService).withdraw(bankAccount, BigDecimal.ONE);

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_PARAMETER_PATH,
                WithdrawController.SERVICE_PATH,
                bankAccount);
        mockMvc.perform(post(
                servicePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataUtils.MAPPER.writeValueAsString(amountDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testWithdrawInternalError() throws Exception {
        Long bankAccount = 1L;
        AmountDto amountDto = AmountDto.builder().amount(BigDecimal.ONE).build();

        Mockito.doThrow(new RuntimeException("testWithdrawInternalError"))
                .when(withdrawService).withdraw(bankAccount, BigDecimal.ONE);

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_PARAMETER_PATH,
                WithdrawController.SERVICE_PATH,
                bankAccount);
        mockMvc.perform(post(
                servicePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataUtils.MAPPER.writeValueAsString(amountDto)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}