package com.endava.endabank.service;

import com.endava.endabank.dto.CreateBankAccountDto;

import java.util.Map;

public interface BankAccountService {
    Map<String, String> save(CreateBankAccountDto banckAccount);
}
