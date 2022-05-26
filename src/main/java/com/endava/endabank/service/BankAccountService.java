package com.endava.endabank.service;

import com.endava.endabank.dto.BankAccountDto;
import com.endava.endabank.dto.TransactionDto;

import java.util.List;

public interface BankAccountService {
    BankAccountDto getAccountDetails(Integer id);
    List<TransactionDto> getTransactionsSummary(Integer id);
}
