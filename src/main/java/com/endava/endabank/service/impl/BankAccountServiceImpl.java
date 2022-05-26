package com.endava.endabank.service.impl;

import com.endava.endabank.constants.AccountTypes;
import com.endava.endabank.dao.BankAccountDao;
import com.endava.endabank.model.AccountType;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.User;
import com.endava.endabank.service.AccountTypeService;
import com.endava.endabank.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountDao bankAccountDao;
    private AccountTypeService accountTypeService;
    private PasswordEncoder passwordEncoder;

    @Override
    public BankAccount save(User user) {
        BankAccount account = new BankAccount();
        String accountNumber = genereteRamdomNumber(16);
        while(bankAccountDao.findByAccountNumber(accountNumber).isPresent()){
            accountNumber = genereteRamdomNumber(16);
        }
        account.setAccountNumber(accountNumber);
        AccountType accountType = accountTypeService.findById(AccountTypes.DEBIT);
        account.setBalance(1000000F);
        account.setAccountType(accountType);
        account.setPassword(passwordEncoder.encode(genereteRamdomNumber(4)));
        account.setUser(user);
        return bankAccountDao.save(account);
    }

    public String genereteRamdomNumber(Integer len){
        char [] chars = "0123456789".toCharArray();
        int charsLength = chars.length;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder();
        for (int i=0;i<len;i++){
            buffer.append(chars[random.nextInt(charsLength)]);
        }
        return buffer.toString();
    }
}
