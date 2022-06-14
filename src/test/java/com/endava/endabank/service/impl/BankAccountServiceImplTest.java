package com.endava.endabank.service.impl;

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
import com.endava.endabank.utils.Pagination;
import com.endava.endabank.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    @Test
    void testFindByAccountNumberShouldThrowException() {
        BigInteger accountNumber = BigInteger.valueOf(Long.parseLong("1111111111111111"));
        when(bankAccountDao.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bankAccountService.findByAccountNumber(accountNumber));
    }

    @Test
    void testFindByAccountNumberShouldSuccess() {
        BankAccount bankAccount = TestUtils.getBankAccount();
        when(bankAccountDao.findByAccountNumber(bankAccount.getAccountNumber())).thenReturn(Optional.of(bankAccount));
        BankAccount bankAccount1 = bankAccountService.findByAccountNumber(bankAccount.getAccountNumber());
        assertEquals(bankAccount, bankAccount1);
    }
    @Test
    void testFindByUserShouldFailWhenBankAccountNotFound() {
        User user = TestUtils.getUserNotAdmin();
        assertThrows(ResourceNotFoundException.class, () -> bankAccountService.findByUser(user));
    }
    @Test
    void testFindByUserShouldSuccessWhenDataCorrect() {
        User user = TestUtils.getUserNotAdmin();
        BankAccount bankAccount = TestUtils.getBankAccount();
        bankAccount.setUser(user);
        when(bankAccountDao.findByUser(user)).thenReturn(Optional.of(bankAccount));
        BankAccount bankAccount1 = bankAccountService.findByUser(user);
        assertEquals(bankAccount, bankAccount1);
    }
    @Test
    void reduceBalance() {
        BankAccount bankAccount = TestUtils.getBankAccount();
        bankAccountService.reduceBalance(bankAccount, 10000.0);
        assertEquals(990000.0, bankAccount.getBalance());
    }

    @Test
    void increaseBalance() {
        BankAccount bankAccount = TestUtils.getBankAccount();
        bankAccountService.increaseBalance(bankAccount, 10000.0);
        assertEquals(1010000.0, bankAccount.getBalance());
    }


    @Test
    void testFindBankAccountUserShouldFailWhenUserNotFound() {
        String email = TestUtils.getUserNotAdmin().getEmail();
        when(userDao.findByEmail(TestUtils.getUserNotAdmin().getEmail())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> bankAccountService.findBankAccountUser(email));
    }

    @Test
    void testFindBankAccountUserShouldFailWhenUserHasNoBankAccount() {
        User user = TestUtils.getUserNotAdmin();
        user.setBankAccounts(emptyList());
        String email = user.getEmail();
        when(userDao.findByEmail(email)).thenReturn(Optional.of(user));
        assertThrows(BadDataException.class, () -> bankAccountService.findBankAccountUser(email));
    }
    @Test
    void testGetAccountDetailsShouldFailWhenEmailNotCorrect() {
        String email = TestUtils.getUserNotAdmin().getEmail();
        assertThrows(AuthenticationException.class, () -> bankAccountService.getAccountDetails("",email));
    }
    @Test
    void testGetAccountSummaryShouldFailWhenEmailNotCorrect() {
        String email = TestUtils.getUserNotAdmin().getEmail();
        assertThrows(AuthenticationException.class, () -> bankAccountService.getTransactionsSummary("",email, 1));
    }
    @Test
    void testFindBankAccountUserShouldSuccessWhenDataCorrect() {
        User user = TestUtils.getUserNotAdmin();
        String email = user.getEmail();
        ArrayList<BankAccount> bankAccounts = new ArrayList<>();
        bankAccounts.add(TestUtils.getBankAccount());
        user.setBankAccounts(bankAccounts);
        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(modelMapper.map(user.getBankAccounts().get(0), BankAccountDto.class)).thenReturn(TestUtils.getBankAccountDto());
        BankAccountDto bankAccountsUser = bankAccountService.getAccountDetails(email,email);
        assertEquals(bankAccountsUser.getId(), user.getBankAccounts().get(0).getId());
        assertEquals(bankAccountsUser.getBalance(), user.getBankAccounts().get(0).getBalance());
    }

    @Test
    void testGetTransactionSummaryShouldSuccessWhenDataCorrect() {
        User user = TestUtils.getUserNotAdmin();
        String email = user.getEmail();
        ArrayList<BankAccount> bankAccounts = new ArrayList<>();
        bankAccounts.add(TestUtils.getBankAccount());
        user.setBankAccounts(bankAccounts);
        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Sort sort = Sort.by(Strings.ACCOUNT_SUMMARY_SORT).descending();
        when(transactionDao.getListTransactionsSummary(1, pagination.getPageable(0, sort))).thenReturn(Page.empty());
        Page<TransactionDto> transactionDaoPage = bankAccountService.getTransactionsSummary(email,email, 1);
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
}

