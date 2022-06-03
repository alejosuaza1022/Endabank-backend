package com.endava.endabank.utils;

import com.endava.endabank.dao.BankAccountDao;
import com.endava.endabank.model.BankAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class BankAccountUtilsTest {

    @Mock
    private BankAccountDao bankAccountDao;

    @Test
    void testGenereteRamdomNumberShouldSuccessWhenDataCorrect() {
        String number = BankAccountUtils.genereteRamdomNumber(4);
        assertEquals(number.length(),4);
    }

    @Test
    void testValidateAccountNumberShouldReturnAccountSuccess() {
        BankAccount account = TestUtils.getBankAccount();
        when(bankAccountDao.findByAccountNumber(any())).thenReturn(Optional.empty());
        BigInteger accountNumber = BankAccountUtils.validateAccountNumber(account.getAccountNumber(),bankAccountDao);
        assertNotNull(accountNumber);
    }

    @Test
    void testValidateAccountNumberShouldGenerateAnotherAccountWhenAlreadyExist() {
        BankAccount account = TestUtils.getBankAccount();
        account.setAccountNumber(BigInteger.valueOf(Long.parseLong("10000000000000")));
        when(bankAccountDao.findByAccountNumber(any())).thenReturn(Optional.of(account)).thenReturn(Optional.empty());
        BigInteger accountNumber = BankAccountUtils.validateAccountNumber(account.getAccountNumber(),bankAccountDao);
        assertNotNull(accountNumber);
    }

    @Test
    void testValidateAccountNumberShouldGenerateAnotherAccountWhenIsLess() {
        BankAccount account = TestUtils.getBadBankAccount();
        BigInteger accountNumber = BankAccountUtils.validateAccountNumber(account.getAccountNumber(),bankAccountDao);
        assertNotNull(accountNumber);
    }
}