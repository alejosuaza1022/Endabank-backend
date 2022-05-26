package com.endava.endabank.service;

import com.endava.endabank.dto.BankAccountDto;
import com.endava.endabank.dto.TransactionDto;

public interface BankAccountService {
    BankAccountDto getAccountDetails(Integer id);/*
    TransactionDto getTransactionsSummary(Integer id);*/
}
