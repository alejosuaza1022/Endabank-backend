package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.BankAccountDao;
import com.endava.endabank.dao.UserDao;
import com.endava.endabank.dto.BankAccountDto;
import com.endava.endabank.exceptions.custom.BadDataException;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.User;
import com.endava.endabank.service.BankAccountService;
import com.endava.endabank.service.UserService;
import com.google.common.annotations.VisibleForTesting;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountDao bankAccountDao;
    private ModelMapper modelMapper;

    public BankAccount findByUserId(Integer id) {
        return bankAccountDao.findByUserId(id).
                orElseThrow(() -> new UsernameNotFoundException(Strings.USER_NOT_FOUND));
    }
    @Override
    @Transactional(readOnly = true)
    public BankAccountDto getAccountDetails(Integer id) {
        BankAccount userBankAccount = findByUserId(id);
        if(userBankAccount == null){
            throw new BadDataException(Strings.ACCOUNT_NOT_FOUND);
        }
        return modelMapper.map(userBankAccount,BankAccountDto.class);
    }

}
