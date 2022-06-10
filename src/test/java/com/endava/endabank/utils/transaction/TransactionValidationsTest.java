package com.endava.endabank.utils.transaction;

import com.endava.endabank.dto.transaction.TransactionCreateDto;
import com.endava.endabank.dto.transaction.TransactionFromMerchantDto;
import com.endava.endabank.exceptions.custom.BadDataException;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.utils.TestUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class TransactionValidationsTest {
    @InjectMocks
    private TransactionValidations transactionValidations;

    @Test
    void testValidateAmountShouldThrowBadDataException() {
        Assert.assertThrows(BadDataException.class, () -> transactionValidations.validateAmount(0.0));
        Assert.assertThrows(BadDataException.class, () -> transactionValidations.validateAmount(-10.0));
    }

    @Test
    void testValidateBankAccountOwnerShouldThrowBadDataException() {
        BankAccount bankAccount = TestUtils.getBankAccount();
        Assert.assertThrows(BadDataException.class, () -> transactionValidations.validateOwner(2, bankAccount));
    }

    @Test
    void testValidatedTransactionsShouldSuccess() {
        TransactionCreateDto transactionCreateDto = TestUtils.getTransactionCreateDto();
        BankAccount bankAccountIssuer = TestUtils.getBankAccount();
        BankAccount bankAccountReciver = TestUtils.getBankAccount2();
        TransactionValidations transactionValidations1 = Mockito.spy(transactionValidations);
        transactionValidations1.validateTransaction(1, bankAccountIssuer, bankAccountReciver, transactionCreateDto);
        verify(transactionValidations1, Mockito.times(1)).validateAmount(transactionCreateDto.getAmount());
        verify(transactionValidations1, Mockito.times(1)).validateOwner(1, bankAccountIssuer);
        verify(transactionValidations1, Mockito.times(1)).validateSameAccount(bankAccountIssuer, bankAccountReciver);
    }

    @Test
    void validateSameAccount() {
        BankAccount bankAccountIssuer = TestUtils.getBankAccount();
        BankAccount bankAccountReceiver = TestUtils.getBankAccount();
        Assert.assertThrows(BadDataException.class, () -> transactionValidations.validateSameAccount(bankAccountIssuer, bankAccountReceiver));
    }
    @Test
    void testValidateExternalTransactionShouldFailWhenUserNotEqual() {
        TransactionFromMerchantDto transactionFromMerchant = TestUtils.getTransactionFromMerchantDto();
        Assert.assertThrows(BadDataException.class, () -> transactionValidations.validateExternalTransaction(1, 2, "1", transactionFromMerchant));
    }
    @Test
    void testValidateExternalTransactionShouldFailWhenApiIdNotEqual() {
        TransactionFromMerchantDto transactionFromMerchant = TestUtils.getTransactionFromMerchantDto();
        Assert.assertThrows(BadDataException.class, () -> transactionValidations.validateExternalTransaction(1, 1, "2", transactionFromMerchant));
    }
    @Test
    void testValidateExternalTransactionShouldSuccessWhenDataCorrect() {
        transactionValidations.validateExternalTransaction(1, 1, "12345", TestUtils.getTransactionFromMerchantDto());
    }
}