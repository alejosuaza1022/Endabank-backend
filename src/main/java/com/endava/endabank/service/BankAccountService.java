package com.endava.endabank.service;

import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.User;

public interface BankAccountService {
    BankAccount save(User user);
}
