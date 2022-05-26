package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.AccountTypeDao;
import com.endava.endabank.dao.IdentifierTypeDao;
import com.endava.endabank.exceptions.custom.ResourceNotFoundException;
import com.endava.endabank.model.AccountType;
import com.endava.endabank.model.IdentifierType;
import com.endava.endabank.service.AccountTypeService;
import com.endava.endabank.service.IdentifierTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AccountTypeServiceImpl implements AccountTypeService {
    private AccountTypeDao accountTypeDao;

    @Override
    @Transactional(readOnly = true)
    public AccountType findById(Integer id) {
        return accountTypeDao.findById(id).
                orElseThrow((() -> new ResourceNotFoundException(Strings.ACCOUNT_TYPE_NOT_FOUND)));
    }
}
