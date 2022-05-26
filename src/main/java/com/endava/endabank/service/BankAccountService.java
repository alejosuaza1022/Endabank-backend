package com.endava.endabank.service;

import com.endava.endabank.dto.BankAccountDto;
import com.endava.endabank.model.BankAccount;

public interface BankAccountService {
    BankAccount findByUserId(Integer id);
    BankAccountDto getAccountDetails(Integer id);
}
