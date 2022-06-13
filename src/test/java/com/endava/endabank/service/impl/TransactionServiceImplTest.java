package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.TransactionDao;
import com.endava.endabank.dto.transaction.TransactionCreateDto;
import com.endava.endabank.dto.transaction.TransactionCreatedDto;
import com.endava.endabank.dto.transaction.TransactionFromMerchantDto;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.Merchant;
import com.endava.endabank.model.StateType;
import com.endava.endabank.model.Transaction;
import com.endava.endabank.service.BankAccountService;
import com.endava.endabank.service.MerchantService;
import com.endava.endabank.service.UserService;
import com.endava.endabank.utils.TestUtils;
import com.endava.endabank.utils.transaction.TransactionValidations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransactionServiceImplTest {

    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private UserService userService;
    @Mock
    private Merchant merchant;
    @Mock
    private MerchantService merchantService;
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
    @Test
    void testCreateTransactionFromMerchantShouldSuccess() {
        Transaction transaction = TestUtils.getTransaction();
        BankAccount bankAccountIssuer = TestUtils.getBankAccount();
        BankAccount bankAccountReceiver = TestUtils.getBankAccount2();
        TransactionFromMerchantDto transactionFromMerchantDto = TestUtils.getTransactionFromMerchantDto();
        when(userService.findByIdentifier(any())).thenReturn(TestUtils.getUserNotAdmin());
        when(merchantService.findByMerchantKey(any())).thenReturn(TestUtils.getMerchantNotReviewed());
        when(bankAccountService.findByUser(any())).thenReturn(bankAccountIssuer);
        when(bankAccountService.findByAccountNumber(transaction.getBankAccountIssuer().getAccountNumber())).
                thenReturn(bankAccountIssuer);
        when(bankAccountService.findByAccountNumber(transaction.getBankAccountReceiver().getAccountNumber())).
                thenReturn(bankAccountReceiver);
        when(transactionDao.save(any(Transaction.class))).thenReturn(transaction);
        when(modelMapper.map(transaction, TransactionCreatedDto.class)).thenReturn(TestUtils.getTransactionCreatedDto());
        Map<String, Object> transactionCreatedDto = transactionService.createTransactionFromMerchant(1,transactionFromMerchantDto);
        assertNotNull(transactionCreatedDto);
    }
    @Test
    void testCreateTransactionFromMerchantShouldFailWhenDataNotCorrect(){
        Transaction transaction = TestUtils.getTransaction();
        BankAccount bankAccountIssuer = TestUtils.getBankAccount();
        BankAccount bankAccountReceiver = TestUtils.getBankAccount2();
        TransactionFromMerchantDto transactionFromMerchantDto = TestUtils.getTransactionFromMerchantDtoWithBadAmount();
        when(userService.findByIdentifier(any())).thenReturn(TestUtils.getUserNotAdmin());
        when(merchantService.findByMerchantKey(any())).thenReturn(TestUtils.getMerchantNotReviewed());
        when(bankAccountService.findByUser(any())).thenReturn(bankAccountIssuer);
        when(bankAccountService.findByAccountNumber(transaction.getBankAccountIssuer().getAccountNumber())).
                thenReturn(bankAccountIssuer);
        when(bankAccountService.findByAccountNumber(transaction.getBankAccountReceiver().getAccountNumber())).
                thenReturn(bankAccountReceiver);
        when(transactionDao.save(any(Transaction.class))).thenReturn(transaction);
        when(modelMapper.map(transaction, TransactionCreatedDto.class)).thenReturn(TestUtils.getTransactionNotCreatedDto());
        Map<String, Object> transactionCreatedDto = transactionService.createTransactionFromMerchant(1,transactionFromMerchantDto);
        assertNotNull(transactionCreatedDto);
    }
}