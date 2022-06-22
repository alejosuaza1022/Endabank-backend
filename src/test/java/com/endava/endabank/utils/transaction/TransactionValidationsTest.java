package com.endava.endabank.utils.transaction;

import com.endava.endabank.constants.Strings;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    void testValidateExternalTransactionShouldSuccess() {
        TransactionFromMerchantDto transferFromMerchantDto = TestUtils.getTransactionFromMerchantDto();
        BankAccount bankAccountIssuer = TestUtils.getBankAccount();
        BankAccount bankAccountReceiver = TestUtils.getBankAccount2();
        String response = transactionValidations.validateExternalTransaction(1, 1, "12345", bankAccountIssuer, bankAccountReceiver, transferFromMerchantDto);
        assertNull(response);
    }
    @Test
    void testValidateExternalTransactionShouldFailWhenUserNotCorrect() {
        TransactionFromMerchantDto transferFromMerchantDto = TestUtils.getTransactionFromMerchantDto();
        BankAccount bankAccountIssuer = TestUtils.getBankAccount();
        BankAccount bankAccountReceiver = TestUtils.getBankAccount2();
        String response = transactionValidations.validateExternalTransaction(1, 2, "12345", bankAccountIssuer, bankAccountReceiver, transferFromMerchantDto);
        assertEquals(Strings.BAD_USER_DATA, response);
    }
    @Test
    void testValidateExternalTransactionShouldFailWhenApiIdNotCorrect() {
        TransactionFromMerchantDto transferFromMerchantDto = TestUtils.getTransactionFromMerchantDto();
        BankAccount bankAccountIssuer = TestUtils.getBankAccount();
        BankAccount bankAccountReceiver = TestUtils.getBankAccount2();
        String response = transactionValidations.validateExternalTransaction(1, 1, "1234", bankAccountIssuer, bankAccountReceiver, transferFromMerchantDto);
        assertEquals(Strings.BAD_API_ID, response);
    }
    @Test
    void testValidateOwnerMShouldFailWhenUserNotCorrect() {
        BankAccount bankAccount = TestUtils.getBankAccount();
        TransactionFromMerchantDto transferFromMerchantDto = TestUtils.getTransactionFromMerchantDto();
        String validate= transactionValidations.validateExternalTransaction(2, 2, "12345", bankAccount, bankAccount, transferFromMerchantDto);
        assertEquals(Strings.USER_NOT_OWN_BANK_ACCOUNT, validate);
    }
    @Test
    void testAmountNotValidShouldFailWhenAmountNotCorrect() {
        TransactionFromMerchantDto transferFromMerchantDto = TestUtils.getTransactionFromMerchantDtoWithBadAmount();
        BankAccount bankAccountIssuer = TestUtils.getBankAccount();
        BankAccount bankAccountReceiver = TestUtils.getBankAccount2();
        String response = transactionValidations.validateExternalTransaction(1, 1, "12345", bankAccountIssuer, bankAccountReceiver, transferFromMerchantDto);
        assertEquals(Strings.AMOUNT_NOT_VALID, response);
    }
    @Test
    void testValidateSameAccountMShouldFailWhenAccountsAreSame() {
        BankAccount bankAccountIssuer = TestUtils.getBankAccount();
        BankAccount bankAccountReceiver = TestUtils.getBankAccount();
        String validate= transactionValidations.validateSameAccountM(bankAccountIssuer, bankAccountReceiver);
        assertEquals(Strings.TRANSACTION_SAME_ACCOUNT, validate);
    }
}