package com.endava.endabank.service;

import com.endava.endabank.dto.transaction.PayTransactionCreatedDto;
import com.endava.endabank.dto.transaction.TransactionCreateDto;
import com.endava.endabank.dto.transaction.TransactionCreatedDto;
import com.endava.endabank.dto.transaction.TransactionFromMerchantDto;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.StateType;
import com.endava.endabank.model.Transaction;

public interface TransactionService {
    TransactionCreatedDto createTransaction(Integer userId, TransactionCreateDto transactionCreateDto);

    Transaction setStateAndBalanceOfTransaction(Transaction transaction, BankAccount bankAccountIssuer,
                                         BankAccount bankAccountReceiver, Double amount);

    PayTransactionCreatedDto createTransactionFromMerchant(Integer userId, TransactionFromMerchantDto transferFromMerchantDto);

    PayTransactionCreatedDto saveTransactionAndState(Transaction transaction, PayTransactionCreatedDto transactionCreatedDto,
                                                     StateType stateType, String description);
}
