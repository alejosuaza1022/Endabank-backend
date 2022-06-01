package com.endava.endabank.service;

import com.endava.endabank.dto.CreateBankAccountDto;
import com.endava.endabank.dto.BankAccountDto;
import com.endava.endabank.dto.TransactionDto;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface BankAccountService {
    Map<String, String> save(CreateBankAccountDto banckAccount);
    BankAccountDto getAccountDetails(String email);
    Page<TransactionDto> getTransactionsSummary(String email, Integer page);
}
