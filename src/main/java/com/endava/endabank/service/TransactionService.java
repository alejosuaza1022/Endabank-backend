package com.endava.endabank.service;

import com.endava.endabank.dto.Transaction.TransactionCreateDto;
import com.endava.endabank.dto.Transaction.TransactionCreatedDto;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.Transaction;

public interface TransactionService {
    TransactionCreatedDto createTransaction(Integer userId, TransactionCreateDto transactionCreateDto);
    void setStateAndBalanceOfTransaction(Transaction transaction, BankAccount bankAccountIssuer,
                                   BankAccount bankAccountReceiver,TransactionCreateDto transactionCreateDto);
}
