package com.rabobank.bankaccountmanager.controller;

import com.rabobank.bankaccountmanager.domain.dto.AmountDto;
import com.rabobank.bankaccountmanager.service.TransferService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public void transfer(@ApiParam(value = "The ID of the from bank account") @PathVariable(name = "fromBankAccountId") Long fromBankAccountId,
                         @ApiParam(value = "The ID of the to bank account") @PathVariable(name = "toBankAccountId") Long toBankAccountId,
                         @ApiParam(value = "The amount of the withdraw transaction") @RequestBody @Valid AmountDto amountDto) {
        LOG.info("/{}/{}/{} called with amount: {}", SERVICE_PATH, fromBankAccountId, toBankAccountId, amountDto);

        transferService.transfer(fromBankAccountId, toBankAccountId, amountDto.getAmount());
    }
}
