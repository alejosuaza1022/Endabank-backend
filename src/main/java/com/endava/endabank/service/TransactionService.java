package com.endava.endabank.service;

import com.endava.endabank.model.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> findAllByBankAccountIssuerOrBankAccountReceiver(String bankAccountIssuer, String bankAccountReceiver);
}
