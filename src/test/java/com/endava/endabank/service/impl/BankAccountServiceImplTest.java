package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.BankAccountDao;
import com.endava.endabank.dto.CreateBankAccountDto;
import com.endava.endabank.model.AccountType;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.service.AccountTypeService;
import com.endava.endabank.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {
    @Mock
    private BankAccountDao bankAccountDao;

    @Mock
    private AccountTypeService accountTypeService;
    @Mock
    private PasswordEncoder passwordEncoder;
    private BankAccountServiceImpl bankAccount;

    @BeforeEach
    void setUp() {
        bankAccount =
                new BankAccountServiceImpl(bankAccountDao,accountTypeService,passwordEncoder);
    }

    @Test
    void testSaveShouldSuccessWhenDataCorrect() {
        AccountType accountType = TestUtils.getAccountType();
        CreateBankAccountDto createBankAccountDto = TestUtils.getCreateBankAccountDto();
        when(accountTypeService.findById(1)).thenReturn(accountType);
        when(bankAccountDao.findByAccountNumber(any())).thenReturn(Optional.empty());
        Map<String, String> map = bankAccount.save(createBankAccountDto);
        assertEquals(Strings.ACCOUNT_CREATED, map.get(Strings.MESSAGE_RESPONSE));
    }

    @Test
    void testSaveShouldCreateAnotherNumberWhenAlreadyExist() {
        BankAccount account = TestUtils.getBankAccount();
        AccountType accountType = TestUtils.getAccountType();
        CreateBankAccountDto createBankAccountDto = TestUtils.getCreateBankAccountDto();
        when(accountTypeService.findById(1)).thenReturn(accountType);
        when(bankAccountDao.findByAccountNumber(any())).thenReturn(Optional.ofNullable(account)).thenReturn(Optional.empty());
        Map<String, String> map = bankAccount.save(createBankAccountDto);
        assertEquals(Strings.ACCOUNT_CREATED, map.get(Strings.MESSAGE_RESPONSE));
    }

    @Test
    void testGenereteRamdomNumberShouldSuccessWhenDataCorrect() {
        String number = bankAccount.genereteRamdomNumber(4);
        assertEquals(number.length(),4);
    }
}