package com.endava.endabank.service.impl;

import com.endava.endabank.constants.AccountTypes;
import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.BankAccountDao;
import com.endava.endabank.dao.TransactionDao;
import com.endava.endabank.dao.UserDao;
import com.endava.endabank.dto.bankaccount.BankAccountDto;
import com.endava.endabank.dto.bankaccount.CreateBankAccountDto;
import com.endava.endabank.dto.TransactionDto;
import com.endava.endabank.exceptions.custom.BadDataException;
import com.endava.endabank.exceptions.custom.ResourceNotFoundException;
import com.endava.endabank.model.AccountType;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.User;
import com.endava.endabank.service.AccountTypeService;
import com.endava.endabank.service.BankAccountService;
import com.endava.endabank.utils.bankaccount.BankAccountUtils;
import com.endava.endabank.utils.Pagination;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountDao bankAccountDao;
    private AccountTypeService accountTypeService;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;
    private UserDao userDao;
    private TransactionDao transactionDao;
    private Pagination pagination;

    @Override
    public BankAccount findByAccountNumber(BigInteger accountNumber) {
        return bankAccountDao.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(Strings.BANK_ACCOUNT_NOT_FOUND));
    }

    @Override
    public BankAccount findByUser(User user) {
        return bankAccountDao.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(Strings.STATUS_ERROR + ": " +Strings.BANK_ACCOUNT_NOT_FOUND));
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

    @Override
    public Map<String, String> save(CreateBankAccountDto bankAccountDto) {
        BankAccount account = new BankAccount();
        AccountType accountType = accountTypeService.findById(AccountTypes.DEBIT);
        BigInteger accountNumber = BigInteger.valueOf(Long.parseLong(BankAccountUtils.genereteRamdomNumber(16)));
        account.setAccountNumber(BankAccountUtils.validateAccountNumber(accountNumber,bankAccountDao));
        account.setBalance(1000000.0);
        account.setAccountType(accountType);
        account.setPassword(passwordEncoder.encode(BankAccountUtils.genereteRamdomNumber(4)));
        account.setUser(bankAccountDto.getUser());
        bankAccountDao.save(account);
        Map<String, String> map = new HashMap<>();
        map.put(Strings.MESSAGE_RESPONSE, Strings.ACCOUNT_CREATED);
        return map;
    }

    public BankAccount findBankAccountUser(String email) {
        User user = userDao.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(Strings.USER_NOT_FOUND));
        List<BankAccount> bankAccount = user.getBankAccounts();
        if (bankAccount.isEmpty()) {
            throw new BadDataException(Strings.ACCOUNT_NOT_FOUND);
        }
        return bankAccount.get(0);//When there is more than one bank account, it can be received as an input parameter.
    }

    @Override
    @Transactional(readOnly = true)
    public BankAccountDto getAccountDetails(String email) {
        BankAccount userBankAccount = findBankAccountUser(email);
        return modelMapper.map(userBankAccount, BankAccountDto.class);
    }

    @Override
    public Page<TransactionDto> getTransactionsSummary(String email, Integer page) {
        BankAccount userBankAccount = findBankAccountUser(email);
        Sort sort = Sort.by(Strings.ACCOUNT_SUMMARY_SORT).descending();
        return transactionDao.getListTransactionsSummary(userBankAccount.getId(), pagination.getPageable(page, sort));
    }
}
