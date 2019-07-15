package com.rabobank.bankaccountmanager.controller;

import com.rabobank.bankaccountmanager.TestDataUtils;
import com.rabobank.bankaccountmanager.domain.dto.CustomerDto;
import com.rabobank.bankaccountmanager.domain.model.Customer;
import com.rabobank.bankaccountmanager.service.CustomerService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;
    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(SBAControllerAdvice.class).build();
    }

    @Test
    public void testGetCustomer() throws Exception {

        Customer customer = TestDataUtils.getCustomer1();
        customer.setId(TestDataUtils.CUSTOMER_ID_FOR_NEW_BANK_ACCOUNT);

        CustomerDto customerDto = TestDataUtils.getCustomerDto1();

        Mockito.when(customerService.getCustomer(customer.getId())).thenReturn(customer);
        Mockito.when(conversionService.convert(customer, CustomerDto.class)).thenReturn(customerDto);

        MvcResult result = mockMvc
                .perform(get(String.format(TestDataUtils.FORMAT_SERVICE_PARAMETER_PATH, CustomerController.SERVICE_PATH, customer.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        CustomerDto actualCustomerDto = TestDataUtils.MAPPER.readValue(result.getResponse().getContentAsString(), CustomerDto.class);
        Assert.assertEquals(TestDataUtils.MSG_ACCOUNT_NUMBER_MISMATCHED, customerDto.getFirstName(), actualCustomerDto.getFirstName());
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        String servicePath = String.format(TestDataUtils.FORMAT_SERVICE_PARAMETER_PATH,
                CustomerController.SERVICE_PATH,
                CustomerController.METHOD_GET_ALL);
        mockMvc.perform(get(servicePath).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testSaveCustomer() throws Exception {
        CustomerDto customerDto = TestDataUtils.getCustomerDto1();
        mockMvc.perform(put(String.format(TestDataUtils.FORMAT_SERVICE_PATH, CustomerController.SERVICE_PATH))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataUtils.MAPPER.writeValueAsString(customerDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
