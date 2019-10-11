package com.rabobank.bankaccountmanager.controller;

import com.google.common.base.Preconditions;
import com.rabobank.bankaccountmanager.domain.dto.AmountDto;
import com.rabobank.bankaccountmanager.service.WithdrawService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This class is created to manage withdraw process
 */
@Api("Withdraw services")
@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(WithdrawController.SERVICE_PATH)
public class WithdrawController {

    public static final String SERVICE_PATH = "api/withdraw";

    private WithdrawService withdrawService;

    @ApiOperation(value = "Withdraw from an account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 500, message = "Internal Error.")
    })
    @PostMapping(value = "{bankAccountId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void withdraw(@ApiParam(value = "The ID of the bank account") @PathVariable(name = "bankAccountId") Long bankAccountId,
                         @ApiParam(value = "The amount of the withdraw transaction") @RequestBody @Valid AmountDto amountDto) {
        LOG.info("/{}/{} called with amount: {}", SERVICE_PATH, bankAccountId, amountDto);
        Preconditions.checkNotNull(amountDto, "amountDto can not be null");

        withdrawService.withdraw(bankAccountId, amountDto.getAmount());
    }

}
