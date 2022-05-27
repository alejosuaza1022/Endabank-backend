package com.endava.endabank.service;

import com.endava.endabank.dto.BankAccountDto;
import com.endava.endabank.dto.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BankAccountService {
    BankAccountDto getAccountDetails(Integer id);
    Page<TransactionDto> getTransactionsSummary(Integer id, Integer page);
}
