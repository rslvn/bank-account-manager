package com.rabobank.bankaccountmanager.controller;

import com.google.common.base.Preconditions;
import com.rabobank.bankaccountmanager.domain.dto.BalanceDto;
import com.rabobank.bankaccountmanager.domain.dto.BankAccountDto;
import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import com.rabobank.bankaccountmanager.service.BankAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is created to manage bank account process
 */
@Api("Bank account management services")
@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(BankAccountController.SERVICE_PATH)
public class BankAccountController {

    public static final String SERVICE_PATH = "api/bank/account";
    public static final String METHOD_GET_BALANCE = "/balance";
    private static final String METHOD_GET_BALANCE_WITH_PARAM = "/balance/{bankAccountId}";
    public static final String METHOD_GET_BALANCE_ALL = "/balance/all";

    private BankAccountService bankAccountService;
    private ConversionService conversionService;

    @ApiOperation(value = "Create a new bank account with a credit card or debit card by given customerId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 500, message = "Internal Error.")
    })
    @PutMapping(value = "{customerId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void saveAccount(@ApiParam(value = "The ID of the customer") @PathVariable(name = "customerId") Long customerId,
                            @ApiParam(value = "The number of the customer") @RequestBody BankAccountDto bankAccountDto) {
        LOG.info("/{}/{} called with bankAccountDto: {}", SERVICE_PATH, customerId, bankAccountDto);
        // we used checkArgument instead of checkNotNull because we want an IllegalArgumentException
        Preconditions.checkArgument(bankAccountDto != null, "bankAccountDto can not be null");
        Preconditions.checkArgument(bankAccountDto.getCard() != null, "bankAccountDto.card can not be null");

        BankAccount bankAccount = conversionService.convert(bankAccountDto, BankAccount.class);

        bankAccountService.addBankAccount(customerId, bankAccount);
    }

    @ApiOperation(value = "Retrieves the current balances of all bank accounts", response = BalanceDto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 500, message = "Internal Error.")
    })
    @GetMapping(value = METHOD_GET_BALANCE_ALL)
    public List<BalanceDto> getAllBalances() {
        LOG.info("/{}{} called", SERVICE_PATH, METHOD_GET_BALANCE_ALL);

        return bankAccountService.getBankAccountList().stream()
                .map(bankAccount -> conversionService.convert(bankAccount, BalanceDto.class))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Retrieves the current balance of a bank account", response = BalanceDto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 500, message = "Internal Error.")
    })
    @GetMapping(value = METHOD_GET_BALANCE_WITH_PARAM)
    public BalanceDto getBalance(@ApiParam(value = "The ID of the bank account") @PathVariable(name = "bankAccountId") Long bankAccountId) {
        LOG.info("/{}{}/{} called", SERVICE_PATH, METHOD_GET_BALANCE, bankAccountId);

        BankAccount bankAccount = bankAccountService.getBankAccount(bankAccountId);

        return conversionService.convert(bankAccount, BalanceDto.class);
    }

}
