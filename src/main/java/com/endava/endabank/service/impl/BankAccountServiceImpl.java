package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.BankAccountDao;
import com.endava.endabank.exceptions.custom.ResourceNotFoundException;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountDao bankAccountDao;

    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        return bankAccountDao.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(Strings.BANK_ACCOUNT_NOT_FOUND));
    }

    @Override
    public void reduceBalance(BankAccount bankAccount, Double amount) {
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountDao.save(bankAccount);
    }

    @Override
    public void increaseBalance(BankAccount bankAccount, Double amount) {
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountDao.save(bankAccount);
    }
}
