package com.endava.endabank.service;

import com.endava.endabank.dto.transaction.TransactionCreateDto;
import com.endava.endabank.dto.transaction.TransactionCreatedDto;
import com.endava.endabank.dto.transaction.TransactionFromMerchantDto;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.Transaction;

import java.util.Map;

public interface TransactionService {
    TransactionCreatedDto createTransaction(Integer userId, TransactionCreateDto transactionCreateDto);

    void setStateAndBalanceOfTransaction(Transaction transaction, BankAccount bankAccountIssuer,
                                         BankAccount bankAccountReceiver, Double amount);

    Map<String, Object> createTransactionFromMerchant(Integer userId,TransactionFromMerchantDto transferFromMerchantDto);
}
