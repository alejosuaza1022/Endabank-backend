package com.endava.endabank.service.impl;

import com.endava.endabank.constants.AccountTypes;
import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.BankAccountDao;
import com.endava.endabank.dto.CreateBankAccountDto;
import com.endava.endabank.model.AccountType;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.service.AccountTypeService;
import com.endava.endabank.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountDao bankAccountDao;
    private AccountTypeService accountTypeService;
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> save(CreateBankAccountDto banckAccountDto) {
        BankAccount account = new BankAccount();
        AccountType accountType = accountTypeService.findById(AccountTypes.DEBIT);
        BigInteger accountNumber = BigInteger.valueOf(Long.parseLong(this.genereteRamdomNumber(16)));
        account.setAccountNumber(validateAccountNumber(accountNumber));
        account.setBalance(1000000F);
        account.setAccountType(accountType);
        account.setPassword(passwordEncoder.encode(genereteRamdomNumber(4)));
        account.setUser(banckAccountDto.getUser());
        bankAccountDao.save(account);
        Map<String, String> map = new HashMap<>();
        map.put(Strings.MESSAGE_RESPONSE, Strings.ACCOUNT_CREATED);
        return map;
    }

    public BigInteger validateAccountNumber(BigInteger account){
        BigInteger comp = BigInteger.valueOf(Long.parseLong("1000000000000000"));
        while(bankAccountDao.findByAccountNumber(account).isPresent() || account.compareTo(comp) < 0){
            account = BigInteger.valueOf(Long.parseLong(this.genereteRamdomNumber(16)));
        }
        return account;
    }

    public String genereteRamdomNumber(Integer len){
        char [] chars = "0123456789".toCharArray();
        int charsLength = chars.length;
        SecureRandom random = new SecureRandom ();
        StringBuilder buffer = new StringBuilder();
        for (int i=0;i<len;i++){
            buffer.append(chars[random.nextInt(charsLength)]);
        }
        return buffer.toString();
    }
}
