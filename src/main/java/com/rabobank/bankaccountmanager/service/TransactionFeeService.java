package com.rabobank.bankaccountmanager.service;

import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import com.rabobank.bankaccountmanager.domain.type.CardType;
import com.rabobank.bankaccountmanager.domain.type.TransactionType;
import com.rabobank.bankaccountmanager.exception.BankAccountManagerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Transaction Fee management service
 */
@Slf4j
@Service
public class TransactionFeeService {

    private static final MathContext mathContext = new MathContext(7, RoundingMode.HALF_UP);

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal creditCardTransferFeeMultiplier = BigDecimal.ONE.divide(ONE_HUNDRED, mathContext);
    private static final BigDecimal creditCardWithdrawFeeMultiplier = BigDecimal.ONE.divide(ONE_HUNDRED, mathContext);

    public BigDecimal getFee(TransactionType transactionType, BankAccount bankAccount, BigDecimal amount) {

        if (CardType.DEBIT_CARD.equals(bankAccount.getCard().getCardType())) {
            return BigDecimal.ZERO;
        }

        if (TransactionType.TRANSFER.equals(transactionType)) {
            return creditCardTransferFeeMultiplier.multiply(amount, mathContext);
        } else if (TransactionType.WITHDRAW.equals(transactionType)) {
            return creditCardWithdrawFeeMultiplier.multiply(amount, mathContext);
        }

        throw BankAccountManagerException.to("Unknown transactionType: %s", transactionType);
    }

    public BigDecimal getTotalAmount(BigDecimal amount, BigDecimal fee) {
        return amount.add(fee, mathContext);
    }

}
