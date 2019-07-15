package com.rabobank.bankaccountmanager.controller;

import com.rabobank.bankaccountmanager.exception.BankAccountManagerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class SBAControllerAdvice extends ResponseEntityExceptionHandler {

    /**
     * handles InvalidArgumentExceptions
     *
     * @param e the exception as cause
     */
    @ExceptionHandler(
            IllegalArgumentException.class
    )
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected void handleBadRequest2(RuntimeException e) {
        LOG.error("HttpStatus.BAD_REQUEST", e);
    }

    /**
     * handles InvalidArgumentExceptions
     *
     * @param e the exception as cause
     */
    @ExceptionHandler(
            BankAccountManagerException.class
    )
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected void handleBadRequest(RuntimeException e) {
        LOG.error("HttpStatus.BAD_REQUEST", e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected void handleGeneric(Exception e) {
        LOG.error("HttpStatus.INTERNAL_SERVER_ERROR", e);
    }

}
