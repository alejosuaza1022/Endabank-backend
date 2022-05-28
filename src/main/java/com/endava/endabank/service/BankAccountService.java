package com.endava.endabank.service;

import com.endava.endabank.dto.BankAccountDto;
import com.endava.endabank.dto.TransactionDto;
import org.springframework.data.domain.Page;

public interface BankAccountService {
    BankAccountDto getAccountDetails(Integer id);
    Page<TransactionDto> getTransactionsSummary(Integer id, Integer page);
}
