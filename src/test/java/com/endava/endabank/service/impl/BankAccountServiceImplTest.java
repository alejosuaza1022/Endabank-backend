package com.endava.endabank.service.impl;

import com.endava.endabank.dao.BankAccountDao;
import com.endava.endabank.exceptions.custom.ResourceNotFoundException;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.utils.TestUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {

    @Mock
    private BankAccountDao bankAccountDao;
    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    @Test
    void testFindByAccountNumberShouldThrowException() {
        when(bankAccountDao.findByAccountNumber("123")).thenReturn(Optional.empty());
        Assert.assertThrows(ResourceNotFoundException.class, () -> bankAccountService.findByAccountNumber("123"));
    }
    @Test
    void testFindByAccountNumberShouldSuccess() {
        BankAccount bankAccount = TestUtils.getBankAccount();
        when(bankAccountDao.findByAccountNumber(bankAccount.getAccountNumber())).thenReturn(Optional.of(bankAccount));
        BankAccount bankAccount1 = bankAccountService.findByAccountNumber(bankAccount.getAccountNumber());
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
}