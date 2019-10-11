package com.rabobank.bankaccountmanager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabobank.bankaccountmanager.AbstractTestContainer;
import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.dto.CustomerDto;
import org.junit.Assert;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(initializers = {AbstractTestContainer.Initializer.class})
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class CustomerControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testSaveCustomer() throws Exception {
        CustomerDto customerDto = TestDataUtils.getCustomerDto1();

        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_PATH, CustomerController.SERVICE_PATH);

        mvc.perform(put(servicePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataUtils.MAPPER.writeValueAsString(customerDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testSaveCustomerNoBody() throws Exception {
        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_PATH, CustomerController.SERVICE_PATH);

        mvc.perform(put(servicePath)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_METHOD_PATH,
                CustomerController.SERVICE_PATH,
                CustomerController.METHOD_GET_ALL);

        MvcResult result = mvc.perform(get(servicePath)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<CustomerDto> customerDtoList = TestDataUtils.MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<List<CustomerDto>>() {
        });

        Assert.assertFalse(customerDtoList.isEmpty());
        customerDtoList.forEach(customerDto -> {
            Assert.assertNotNull(customerDto.getFirstName());
            Assert.assertNotNull(customerDto.getLastName());
            Assert.assertNotNull(customerDto.getInitial());
        });
    }

    @Test
    public void testGetCustomer() throws Exception {
        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_PARAMETER_PATH,
                CustomerController.SERVICE_PATH,
                TestDataUtils.CUSTOMER_ID_FOR_NEW_BANK_ACCOUNT);

        MvcResult result = mvc.perform(get(servicePath)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        CustomerDto customerDto = TestDataUtils.MAPPER.readValue(result.getResponse().getContentAsString(), CustomerDto.class);
        Assert.assertNotNull(customerDto);
        Assert.assertNotNull(customerDto.getFirstName());
        Assert.assertNotNull(customerDto.getLastName());
        Assert.assertNotNull(customerDto.getInitial());
    }

    @Test
    public void testGetCustomerNoCustomer() throws Exception {
        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_PARAMETER_PATH,
                CustomerController.SERVICE_PATH,
                TestDataUtils.ID_NOT_EXIST);

        mvc.perform(get(servicePath)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }
}
