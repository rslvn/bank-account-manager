package com.rabobank.bankaccountmanager.controller;

import com.google.common.base.Preconditions;
import com.rabobank.bankaccountmanager.domain.dto.AmountDto;
import com.rabobank.bankaccountmanager.service.TransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * This class is created to manage transfer process
 */
@Api("Transfer services")
@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(TransferController.SERVICE_PATH)
public class TransferController {

    public static final String SERVICE_PATH = "api/transfer";

    private TransferService transferService;

    @ApiOperation(value = "Transfer money from an account to other account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 500, message = "Internal Error.")
    })
    @PostMapping(value = "{fromBankAccountId}/{toBankAccountId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void withdraw(@ApiParam(value = "The ID of the from bank account") @PathVariable(name = "fromBankAccountId") Long fromBankAccountId,
                         @ApiParam(value = "The ID of the to bank account") @PathVariable(name = "toBankAccountId") Long toBankAccountId,
                         @ApiParam(value = "The amount of the withdraw transaction") @RequestBody @Valid AmountDto amountDto) {
        LOG.info("/{}/{}/{} called with amount: {}", SERVICE_PATH, fromBankAccountId, toBankAccountId, amountDto);
        Preconditions.checkNotNull(amountDto, "amountDto can not be null");

        transferService.transfer(fromBankAccountId, toBankAccountId, amountDto.getAmount());
    }
}