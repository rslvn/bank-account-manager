package com.rabobank.bankaccountmanager.service;

import com.google.common.base.Preconditions;
import com.rabobank.bankaccountmanager.domain.model.BankAccount;
import com.rabobank.bankaccountmanager.domain.model.Customer;
import com.rabobank.bankaccountmanager.exception.BankAccountManagerException;
import com.rabobank.bankaccountmanager.repository.BankAccountRepository;
import com.rabobank.bankaccountmanager.util.FormatterUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * BankAccount management service
 */
@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class BankAccountService {

    private static final String MESSAGE_FORMAT_NO_BANK_ACCOUNT = "No bankAccount by bankAccountId: %s";


    private BankAccountRepository bankAccountRepository;
    private CustomerService customerService;

    public BankAccount addBankAccount(Long customerId, BankAccount bankAccount) {
        Preconditions.checkNotNull(bankAccount, "bankAccount can not be null");
        Preconditions.checkNotNull(bankAccount.getCurrentBalance(), "currentBalance can not be null");
        Preconditions.checkArgument(bankAccount.getCurrentBalance().compareTo(BigDecimal.ZERO) > -1, "CurrentBalance can not be negative");

        Customer customer = customerService.getCustomer(customerId);
        bankAccount.setCustomer(customer);
        bankAccount.getCard().setHolderName(FormatterUtil.getCardHolderName(customer));


        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
        LOG.info("A bank account saved for customer: {}", customerId);

        return savedBankAccount;
    }

    public BankAccount getBankAccount(Long bankAccountId) {
        Preconditions.checkNotNull(bankAccountId, MESSAGE_FORMAT_NO_BANK_ACCOUNT, bankAccountId);

        return bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> BankAccountManagerException.to(MESSAGE_FORMAT_NO_BANK_ACCOUNT, bankAccountId));
    }


    public List<BankAccount> getBankAccountList() {
        return bankAccountRepository.findAll();
    }

    public BankAccount decreaseCurrentBalance(BankAccount bankAccount, BigDecimal amount) {
        int effectedRows = bankAccountRepository.decreaseCurrentBalance(bankAccount.getId(), amount);
        if (effectedRows == 0) {
            throw BankAccountManagerException.to(
                    "The bank account is not effected of withdraw");
        }

        return bankAccountRepository.findById(bankAccount.getId())
                .orElseThrow(() -> BankAccountManagerException.to(MESSAGE_FORMAT_NO_BANK_ACCOUNT, bankAccount.getId()));
    }

    public BankAccount increaseCurrentBalance(BankAccount bankAccount, BigDecimal amount) {
        int effectedRows = bankAccountRepository.increaseCurrentBalance(bankAccount.getId(), amount);
        if (effectedRows == 0) {
            throw BankAccountManagerException.to(
                    "The bank account is not effected of transfer");
        }

        return bankAccountRepository.findById(bankAccount.getId())
                .orElseThrow(() -> BankAccountManagerException.to(MESSAGE_FORMAT_NO_BANK_ACCOUNT, bankAccount.getId()));
    }
}
