package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.BankAccountDao;
import com.endava.endabank.dto.CreateBankAccountDto;
import com.endava.endabank.model.AccountType;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.service.AccountTypeService;
import com.endava.endabank.dao.TransactionDao;
import com.endava.endabank.dao.UserDao;
import com.endava.endabank.dto.BankAccountDto;
import com.endava.endabank.dto.TransactionDto;
import com.endava.endabank.exceptions.custom.BadDataException;
import com.endava.endabank.model.User;
import com.endava.endabank.utils.Pagination;
import com.endava.endabank.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {
    @Mock
    private BankAccountDao bankAccountDao;

    @Mock
    private AccountTypeService accountTypeService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserDao userDao;
    @Mock
    private TransactionDao transactionDao;
    @Mock
    private Pagination pagination;
    private BankAccountServiceImpl bankAccountService;

    @BeforeEach
    void setUp(){
        bankAccountService = new BankAccountServiceImpl(bankAccountDao,accountTypeService,passwordEncoder,modelMapper, userDao, transactionDao, pagination);
    }

    @Test
    void testFindBankAccountUserShouldFailWhenUserNotFound(){
        when(userDao.findByEmail(TestUtils.getUserNotAdmin().getEmail())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> bankAccountService.findBankAccountUser(TestUtils.getUserNotAdmin().getEmail()));
    }
    @Test
    void testFindBankAccountUserShouldFailWhenUserHasNoBankAccount(){
        User user = TestUtils.getUserNotAdmin();
        user.setBankAccounts(emptyList());
        String email = user.getEmail();
        when(userDao.findByEmail(email)).thenReturn(Optional.of(user));
        assertThrows(BadDataException.class, () -> bankAccountService.findBankAccountUser(email));
    }
    @Test
    void testFindBankAccountUserShouldSuccessWhenDataCorrect(){
        User user = TestUtils.getUserNotAdmin();
        ArrayList<BankAccount> bankAccounts = new ArrayList<>();
        bankAccounts.add(TestUtils.getBankAccount());
        user.setBankAccounts(bankAccounts);
        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(modelMapper.map(user.getBankAccounts().get(0),BankAccountDto.class)).thenReturn(TestUtils.getBankAccountDto());
        BankAccountDto bankAccountsUser = bankAccountService.getAccountDetails(user.getEmail());
        assertEquals (bankAccountsUser.getId(), user.getBankAccounts().get(0).getId());
        assertEquals(bankAccountsUser.getBalance(), user.getBankAccounts().get(0).getBalance());
    }
    @Test
    void testGetTransactionSummaryShouldSuccessWhenDataCorrect(){
        User user = TestUtils.getUserNotAdmin();
        ArrayList<BankAccount> bankAccounts = new ArrayList<>();
        bankAccounts.add(TestUtils.getBankAccount());
        user.setBankAccounts(bankAccounts);
        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Sort sort = Sort.by(Strings.ACCOUNT_SUMMARY_SORT).descending();
        when(transactionDao.getListTransactionsSummary(1,pagination.getPageable(0,sort))).thenReturn(Page.empty());
        Page<TransactionDto> transactionDaoPage = bankAccountService.getTransactionsSummary(user.getEmail(),1);
        assertEquals(0, transactionDaoPage.getTotalElements());
    }

    @Test
    void testSaveShouldSuccessWhenDataCorrect() {
        AccountType accountType = TestUtils.getAccountType();
        CreateBankAccountDto createBankAccountDto = TestUtils.getCreateBankAccountDto();
        when(accountTypeService.findById(1)).thenReturn(accountType);
        when(bankAccountDao.findByAccountNumber(any())).thenReturn(Optional.empty());
        Map<String, String> map = bankAccountService.save(createBankAccountDto);
        assertEquals(Strings.ACCOUNT_CREATED, map.get(Strings.MESSAGE_RESPONSE));
    }

    @Test
    void testValidateAccountNumberShouldReturnAccountSuccess() {
        BankAccount account = TestUtils.getBankAccount();
        when(bankAccountDao.findByAccountNumber(any())).thenReturn(Optional.empty());
        BigInteger accountNumber = bankAccountService.validateAccountNumber(account.getAccountNumber());
        assertNotNull(accountNumber);
    }

    @Test
    void testValidateAccountNumberShouldGenerateAnotherAccountWhenAlreadyExist() {
        BankAccount account = TestUtils.getBankAccount();
        account.setAccountNumber(BigInteger.valueOf(Long.parseLong("10000000000000")));
        when(bankAccountDao.findByAccountNumber(any())).thenReturn(Optional.of(account)).thenReturn(Optional.empty());
        BigInteger accountNumber = bankAccountService.validateAccountNumber(account.getAccountNumber());
        assertNotNull(accountNumber);
    }

    @Test
    void testValidateAccountNumberShouldGenerateAnotherAccountWhenIsLess() {
        BankAccount account = TestUtils.getBadBankAccount();
        BigInteger accountNumber = bankAccountService.validateAccountNumber(account.getAccountNumber());
        assertNotNull(accountNumber);
    }

    @Test
    void testGenereteRamdomNumberShouldSuccessWhenDataCorrect() {
        String number = bankAccountService.genereteRamdomNumber(4);
        assertEquals(number.length(),4);
    }
}

