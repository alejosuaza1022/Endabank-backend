package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.TransactionDao;
import com.endava.endabank.dto.Transaction.TransactionCreateDto;
import com.endava.endabank.dto.Transaction.TransactionCreatedDto;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.StateType;
import com.endava.endabank.model.Transaction;
import com.endava.endabank.service.BankAccountService;
import com.endava.endabank.utils.TestUtils;
import com.endava.endabank.utils.transaction.TransactionValidations;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private TransactionDao transactionDao;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private EntityManager entityManager;
    @Mock
    private TransactionValidations transactionValidations;
    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void testCreateTransaction() {
        Transaction transaction = TestUtils.getTransaction();
        BankAccount bankAccountIssuer = TestUtils.getBankAccount();
        BankAccount bankAccountReceiver = TestUtils.getBankAccount2();
        TransactionCreateDto transactionCreateDto = TestUtils.getTransactionCreateDto();
        when(bankAccountService.findByAccountNumber(transaction.getBankAccountIssuer().getAccountNumber())).
                thenReturn(bankAccountIssuer);
        when(bankAccountService.findByAccountNumber(transaction.getBankAccountReceiver().getAccountNumber())).
                thenReturn(bankAccountReceiver);
        when(transactionDao.save(any(Transaction.class))).thenReturn(transaction);
        when(modelMapper.map(transaction, TransactionCreatedDto.class)).thenReturn(TestUtils.getTransactionCreatedDto());
        TransactionCreatedDto transactionCreatedDto = transactionService.createTransaction(1,TestUtils.getTransactionCreateDto());
        verify(transactionValidations,times(1)).validateTransaction(1, bankAccountIssuer,
                bankAccountReceiver, transactionCreateDto);
        assertEquals(TestUtils.getTransactionCreatedDto(), transactionCreatedDto);
    }

    @Test
    void testSetStateAndBalanceOfTransactionShouldSuccess() {
        Transaction transaction = TestUtils.getTransaction();
        BankAccount bankAccountIssuer = TestUtils.getBankAccount();
        BankAccount bankAccountReceiver = TestUtils.getBankAccount2();
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_APPROVED_STATE)).thenReturn(TestUtils.getStateTypeApproved());
        transactionService.setStateAndBalanceOfTransaction(transaction, bankAccountIssuer, bankAccountReceiver,
                transaction.getAmount());
        verify(bankAccountService, times(1)).reduceBalance(bankAccountIssuer, transaction.getAmount());
        verify(bankAccountService, times(1)).increaseBalance(bankAccountReceiver, transaction.getAmount());
        assertEquals(transaction.getStateType(), TestUtils.getStateTypeApproved());
        assertEquals(Strings.TRANSACTION_COMPLETED, transaction.getStateDescription());
    }

    @Test
    void testSetStateAndBalanceOfTransactionShouldFailNotFounds() {
        Transaction transaction = TestUtils.getTransaction();
        BankAccount bankAccountIssuer = TestUtils.getBankAccount();
        BankAccount bankAccountReceiver = TestUtils.getBankAccount2();
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_APPROVED_STATE)).thenReturn(TestUtils.getStateTypeApproved());
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_FAILED_STATE)).thenReturn(TestUtils.getStateTypeFailed());
        transactionService.setStateAndBalanceOfTransaction(transaction, bankAccountIssuer, bankAccountReceiver,
                10000001.0);
        verify(bankAccountService, times(0)).reduceBalance(bankAccountIssuer, transaction.getAmount());
        verify(bankAccountService, times(0)).increaseBalance(bankAccountReceiver, transaction.getAmount());
        assertEquals(transaction.getStateType(), TestUtils.getStateTypeFailed());
        assertEquals(Strings.NOT_FOUNDS_ENOUGH, transaction.getStateDescription());
    }
}