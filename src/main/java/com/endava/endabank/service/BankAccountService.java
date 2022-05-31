package com.endava.endabank.service;

import com.endava.endabank.model.BankAccount;

public interface BankAccountService {
    BankAccount findByAccountNumber(String accountNumber);

    void reduceBalance(BankAccount bankAccount, Double amount);

    void increaseBalance(BankAccount bankAccount, Double amount);
}
