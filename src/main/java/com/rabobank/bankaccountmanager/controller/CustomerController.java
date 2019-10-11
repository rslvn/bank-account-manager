package com.rabobank.bankaccountmanager.controller;

import com.google.common.base.Preconditions;
import com.rabobank.bankaccountmanager.domain.dto.CustomerDto;
import com.rabobank.bankaccountmanager.domain.model.Customer;
import com.rabobank.bankaccountmanager.service.CustomerService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is created to manage customer process
 */
@Api("Customer management services")
@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(CustomerController.SERVICE_PATH)
public class CustomerController {

    public static final String SERVICE_PATH = "api/customer";
    public static final String METHOD_GET_ALL = "/all";

    private CustomerService customerService;
    private ConversionService conversionService;

    @ApiOperation(value = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 500, message = "Internal Error.")
    })
    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public void saveCustomer(@ApiParam(value = "The ID of the customer") @RequestBody @Valid CustomerDto customerDto) {
        LOG.info("/{} called with customerId: {}", SERVICE_PATH, customerDto);
        Preconditions.checkNotNull(customerDto, "customerDto can not be null");

        customerService.saveCustomer(conversionService.convert(customerDto, Customer.class));
    }

    @ApiOperation(value = "Retrieves all customers", response = CustomerDto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 500, message = "Internal Error.")
    })
    @GetMapping(value = METHOD_GET_ALL)
    public List<CustomerDto> getAllCustomers() {
        LOG.info("/{}{} called", SERVICE_PATH, METHOD_GET_ALL);

        return customerService.getCustomerList().stream()
                .map(customer -> conversionService.convert(customer, CustomerDto.class))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Retrieve customer by given customerId", response = CustomerDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 500, message = "Internal Error.")
    })
    @GetMapping(value = "/{customerId}")
    public CustomerDto getCustomer(@ApiParam(value = "The ID of the customer") @PathVariable(name = "customerId") Long customerId) {
        LOG.info("/{}/{} called", SERVICE_PATH, customerId);
        Customer customer = customerService.getCustomer(customerId);

        return conversionService.convert(customer, CustomerDto.class);
    }

}
