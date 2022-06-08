package com.endava.endabank.service;

import com.endava.endabank.dto.transaction.TransactionCreateDto;
import com.endava.endabank.dto.transaction.TransactionCreatedDto;
import com.endava.endabank.dto.transaction.TransactionFromMerchantDto;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.Transaction;

public interface TransactionService {
    //List<Transaction> findAllByBankAccountIssuerOrBankAccountReceiver(String bankAccountIssuer, String bankAccountReceiver);

    TransactionCreatedDto createTransaction(Integer userId, TransactionCreateDto transactionCreateDto);

    void setStateAndBalanceOfTransaction(Transaction transaction, BankAccount bankAccountIssuer,
                                         BankAccount bankAccountReceiver, Double amount);

    TransactionCreatedDto createTransactionFromMerchant(TransactionFromMerchantDto transferFromMerchantDto);
}
