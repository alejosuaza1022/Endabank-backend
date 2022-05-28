package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.TransactionDao;
import com.endava.endabank.dao.UserDao;
import com.endava.endabank.dto.BankAccountDto;
import com.endava.endabank.dto.TransactionDto;
import com.endava.endabank.exceptions.custom.BadDataException;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.User;
import com.endava.endabank.utils.Pagination;
import com.endava.endabank.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {
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
        bankAccountService = new BankAccountServiceImpl(modelMapper, userDao, transactionDao, pagination);
    }
    @Test
    void testFindBankAccountUserShouldFailWhenUserNotFound(){
        when(userDao.findById(1)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> bankAccountService.findBankAccountUser(1));
    }
    @Test
    void testFindBankAccountUserShouldFailWhenUserHasNoBankAccount(){
        User user = TestUtils.getUserNotAdmin();
        user.setBankAccounts(emptyList());
        Integer id = user.getId();
        when(userDao.findById(id)).thenReturn(Optional.of(user));
        assertThrows(BadDataException.class, () -> bankAccountService.findBankAccountUser(id));
    }
    @Test
    void testFindBankAccountUserShouldSuccessWhenDataCorrect(){
        User user = TestUtils.getUserNotAdmin();
        ArrayList<BankAccount> bankAccounts = new ArrayList<>();
        bankAccounts.add(TestUtils.getBankAccount());
        user.setBankAccounts(bankAccounts);
        when(userDao.findById(1)).thenReturn(Optional.of(user));
        when(modelMapper.map(user.getBankAccounts().get(0),BankAccountDto.class)).thenReturn(TestUtils.getBankAccountDto());
        BankAccountDto bankAccountsUser = bankAccountService.getAccountDetails(user.getId());
        assertEquals (bankAccountsUser.getId(), user.getBankAccounts().get(0).getId());
        assertEquals(bankAccountsUser.getBalance(), user.getBankAccounts().get(0).getBalance());
    }
    @Test
    void testGetTransactionSummaryShouldSuccessWhenDataCorrect(){
        User user = TestUtils.getUserNotAdmin();
        ArrayList<BankAccount> bankAccounts = new ArrayList<>();
        bankAccounts.add(TestUtils.getBankAccount());
        user.setBankAccounts(bankAccounts);
        when(userDao.findById(1)).thenReturn(Optional.of(user));
        Sort sort = Sort.by(Strings.ACCOUNT_SUMMARY_SORT).descending();
        when(transactionDao.getListTransactionsSummary(1,pagination.getPageable(0,sort))).thenReturn(Page.empty());
        Page<TransactionDto> transactionDaoPage = bankAccountService.getTransactionsSummary(user.getId(),1);
        assertEquals(0, transactionDaoPage.getTotalElements());
    }
}
