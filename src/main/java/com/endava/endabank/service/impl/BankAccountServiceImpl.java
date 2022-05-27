package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.TransactionDao;
import com.endava.endabank.dao.UserDao;
import com.endava.endabank.dto.BankAccountDto;
import com.endava.endabank.dto.TransactionDto;
import com.endava.endabank.exceptions.custom.BadDataException;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.Transaction;
import com.endava.endabank.model.User;
import com.endava.endabank.service.BankAccountService;
import com.endava.endabank.utils.Pagination;
import com.google.common.annotations.VisibleForTesting;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private ModelMapper modelMapper;
    private UserDao userDao;
    private TransactionDao transactionDao;
    private Pagination pagination;

    public BankAccount findBankAccountUser(Integer id){
        User user= userDao.findById(id).orElseThrow(()-> new UsernameNotFoundException(Strings.USER_NOT_FOUND));
        List<BankAccount> bankAccount=user.getBankAccounts();
        if(bankAccount.size() == 0){
            throw new BadDataException(Strings.ACCOUNT_NOT_FOUND);
        }
        return bankAccount.get(0);//When there is more than one bank account, it can be received as an input parameter.
    }
    @Override
    @Transactional(readOnly = true)
    public BankAccountDto getAccountDetails(Integer id) {
        BankAccount userBankAccount = findBankAccountUser(id);
        return modelMapper.map(userBankAccount,BankAccountDto.class);
    }
    @Override
    public Page<TransactionDto> getTransactionsSummary(Integer id, Integer page){
        BankAccount userBankAccount = findBankAccountUser(id);
        Sort sort = Sort.by("create_at").descending();
        return transactionDao.getListTransactionsSummary(userBankAccount.getId(), pagination.getPageable(page,sort));
    }
    @VisibleForTesting
    TransactionDto mapToTransactionsToTransactionDto(Transaction transactions) {
        return modelMapper.map(transactions, TransactionDto.class);
    }
}
