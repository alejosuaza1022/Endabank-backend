package com.endava.endabank.service;

import com.endava.endabank.dto.TransactionDto;
import com.endava.endabank.dto.bankaccount.BankAccountDto;
import com.endava.endabank.dto.bankaccount.CreateBankAccountDto;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.User;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.util.Map;

public interface BankAccountService {
    BankAccount findByAccountNumber(BigInteger accountNumber);

    BankAccount findByUser(User user);

    void reduceBalance(BankAccount bankAccount, Double amount);

    void increaseBalance(BankAccount bankAccount, Double amount);

    Map<String, String> save(CreateBankAccountDto banckAccount);

    BankAccountDto getAccountDetails(String email);

    Page<TransactionDto> getTransactionsSummary(String email, Integer page);
}
