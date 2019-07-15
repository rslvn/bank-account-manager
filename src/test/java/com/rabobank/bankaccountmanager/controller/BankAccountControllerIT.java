package com.rabobank.bankaccountmanager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.dto.BalanceDto;
import com.rabobank.bankaccountmanager.domain.dto.BankAccountDto;
import com.rabobank.bankaccountmanager.domain.dto.CardDto;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(initializers = {BankAccountControllerIT.Initializer.class})
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class BankAccountControllerIT {

    @ClassRule
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres");

    @Autowired
    private MockMvc mvc;

    @Test
    public void testSaveBankAccount() throws Exception {

        BankAccountDto bankAccount = TestDataUtils.getBankAccountDto1();
        CardDto card = TestDataUtils.getDebitCardDto1();

        bankAccount.setCard(card);

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_PARAMETER_PATH,
                BankAccountController.SERVICE_PATH,
                TestDataUtils.CUSTOMER_ID_FOR_NEW_BANK_ACCOUNT);

        mvc.perform(put(servicePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataUtils.MAPPER.writeValueAsString(bankAccount)))
                .andDo(print())
                .andExpect(status().isOk()
                );
    }

    @Test
    public void testSaveBankAccountNoCustomer() throws Exception {

        BankAccountDto bankAccount = TestDataUtils.getBankAccountDto1();
        CardDto card = TestDataUtils.getDebitCardDto1();
        bankAccount.setCard(card);

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_PARAMETER_PATH,
                BankAccountController.SERVICE_PATH,
                TestDataUtils.ID_NOT_EXIST);

        // waits bad request
        mvc.perform(put(servicePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataUtils.MAPPER.writeValueAsString(bankAccount)))
                .andDo(print())
                .andExpect(status().isBadRequest()
                );
    }

    @Test
    public void testSaveBankAccountNoCardInBody() throws Exception {

        BankAccountDto bankAccount = TestDataUtils.getBankAccountDtoNoCard();

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_PARAMETER_PATH,
                BankAccountController.SERVICE_PATH,
                TestDataUtils.ID_NOT_EXIST);

        // waits bad request
        mvc.perform(put(servicePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataUtils.MAPPER.writeValueAsString(bankAccount)))
                .andDo(print())
                .andExpect(status().isBadRequest()
                );
    }

    @Test
    public void testSaveBankAccountNoBody() throws Exception {
        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_PARAMETER_PATH,
                BankAccountController.SERVICE_PATH,
                TestDataUtils.ID_NOT_EXIST);

        // waits bad request
        mvc.perform(put(servicePath)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest()
                );
    }


    @Test
    public void testGetAllBalances() throws Exception {
        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_METHOD_PATH,
                BankAccountController.SERVICE_PATH,
                BankAccountController.METHOD_GET_BALANCE_ALL);

        MvcResult result = mvc.perform(get(servicePath)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()
                ).andReturn();

        List<BalanceDto> balanceDtoList = TestDataUtils.MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<List<BalanceDto>>() {
        });

        Assert.assertFalse(balanceDtoList.isEmpty());
        balanceDtoList.forEach(balanceDto -> {
            Assert.assertNotNull(balanceDto.getBankAccountId());
            Assert.assertNotNull(balanceDto.getCurrentBalance());
        });
    }

    @Test
    public void testBalance() throws Exception {
        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_METHOD_PARAM_PATH,
                BankAccountController.SERVICE_PATH,
                BankAccountController.METHOD_GET_BALANCE,
                TestDataUtils.BANK_ACCOUNT_DEBIT
        );

        // waits bad request
        MvcResult result = mvc.perform(get(servicePath)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()
                ).andReturn();

        BalanceDto balanceDto = TestDataUtils.MAPPER.readValue(result.getResponse().getContentAsString(), BalanceDto.class);

        Assert.assertNotNull(balanceDto);
        Assert.assertNotNull(balanceDto.getBankAccountId());
        Assert.assertNotNull(balanceDto.getCurrentBalance());
    }

    @Test
    public void testBalanceNoBankAccount() throws Exception {
        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_METHOD_PARAM_PATH,
                BankAccountController.SERVICE_PATH,
                BankAccountController.METHOD_GET_BALANCE,
                TestDataUtils.ID_NOT_EXIST
        );

        // waits bad request
        mvc.perform(get(servicePath)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest()
                );
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

}
