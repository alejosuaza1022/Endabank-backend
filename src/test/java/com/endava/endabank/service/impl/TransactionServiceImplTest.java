package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.MerchantDao;
import com.endava.endabank.dao.TransactionDao;
import com.endava.endabank.dao.TransactionStateDao;
import com.endava.endabank.dao.UserDao;
import com.endava.endabank.dto.transaction.PayTransactionCreatedDto;
import com.endava.endabank.dto.transaction.TransactionCreateDto;
import com.endava.endabank.dto.transaction.TransactionCreatedDto;
import com.endava.endabank.dto.transaction.TransactionFromMerchantDto;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.Merchant;
import com.endava.endabank.model.StateType;
import com.endava.endabank.model.Transaction;
import com.endava.endabank.service.BankAccountService;
import com.endava.endabank.service.MerchantService;
import com.endava.endabank.service.TransactionStateService;
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
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    @Mock
    private UserDao userDao;
    @Mock
    private TransactionStateDao transactionStateDao;
    @Mock
    private TransactionStateService transactionStateService;
    @Mock
    private MerchantDao merchantDao;
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
        TransactionFromMerchantDto transactionFromMerchantDto = TestUtils.getTransactionFromMerchantDto();
        when(transactionDao.save(any())).thenReturn(transaction);
        transaction.setCreateAt(LocalDateTime.now());
        transaction.setId(1);
        when(userDao.findByIdentifier(transactionFromMerchantDto.getIdentifier())).thenReturn(Optional.of(TestUtils.getUserNotAdmin()));
        when(transactionStateDao.save(any())).thenReturn(null);
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_PENDING_STATE)).thenReturn(TestUtils.getStateTypePending());
        when(merchantDao.findByMerchantKey(any())).thenReturn(TestUtils.getMerchantOptionalIsApproved());
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_ERROR_STATE)).thenReturn(TestUtils.getStateTypeError());
        when(userDao.findById(any())).thenReturn(Optional.of(TestUtils.getUserAdmin()));
        when(bankAccountService.findByUser(any())).thenReturn(TestUtils.getBankAccount()).thenReturn(TestUtils.getBankAccount2());
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_APPROVED_STATE)).thenReturn(TestUtils.getStateTypeApproved());
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_AUTHORISED_STATE)).thenReturn(TestUtils.getStateTypeAuthorised());
        PayTransactionCreatedDto transactionCreatedDto = transactionService.createTransactionFromMerchant(1,TestUtils.getTransactionFromMerchantDto());
        assertNotNull(transactionCreatedDto);
    }
    @Test
    void testCreateTransactionFromMerchantShouldFailWhenFindByIdentifierIsNull(){
        Transaction transaction = TestUtils.getTransaction();
        TransactionFromMerchantDto transactionFromMerchantDto = TestUtils.getTransactionFromMerchantDto();
        when(transactionDao.save(any())).thenReturn(transaction);
        transaction.setCreateAt(LocalDateTime.now());
        when(userDao.findByIdentifier(transactionFromMerchantDto.getIdentifier())).thenReturn(Optional.empty());
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_REFUSED_STATE)).thenReturn(TestUtils.getStateTypeRefused());
        when(transactionStateDao.save(any())).thenReturn(null);
        when(modelMapper.map(transaction, TransactionCreatedDto.class)).thenReturn(TestUtils.getTransactionCreatedDto());
        PayTransactionCreatedDto transactionCreatedDto = transactionService.createTransactionFromMerchant(1,TestUtils.getTransactionFromMerchantDto());
        assertEquals(Strings.USER_NOT_FOUND, transactionCreatedDto.getStateDescription());
    }
    @Test
    void testCreateTransactionFromMerchantShouldFailWhenMerchantOptionalIsNull(){
        Transaction transaction = TestUtils.getTransaction();
        TransactionFromMerchantDto transactionFromMerchantDto = TestUtils.getTransactionFromMerchantDto();
        when(transactionDao.save(any())).thenReturn(transaction);
        transaction.setCreateAt(LocalDateTime.now());
        when(userDao.findByIdentifier(transactionFromMerchantDto.getIdentifier())).thenReturn(Optional.of(TestUtils.getUserNotAdmin()));
        when(transactionStateDao.save(any())).thenReturn(null);
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_PENDING_STATE)).thenReturn(TestUtils.getStateTypePending());
        when(bankAccountService.findByUser(TestUtils.getUserAdmin())).thenReturn(TestUtils.getBankAccount());
        when(merchantDao.findByMerchantKey(any())).thenReturn(Optional.empty());
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_REFUSED_STATE)).thenReturn(TestUtils.getStateTypeError());
        when(transactionStateDao.save(any())).thenReturn(null);
        when(transactionDao.save(transaction)).thenReturn(null);
        PayTransactionCreatedDto transactionCreatedDto = transactionService.createTransactionFromMerchant(1,TestUtils.getTransactionFromMerchantDto());
        assertEquals(Strings.MERCHANT_NOT_FOUND, transactionCreatedDto.getStateDescription());
    }

    @Test
    void testCreateTransactionFromMerchantShouldFailWhenMerchantIsNotApproved(){
        Transaction transaction = TestUtils.getTransaction();
        TransactionFromMerchantDto transactionFromMerchantDto = TestUtils.getTransactionFromMerchantDto();
        when(transactionDao.save(any())).thenReturn(transaction);
        transaction.setCreateAt(LocalDateTime.now());
        when(userDao.findByIdentifier(transactionFromMerchantDto.getIdentifier())).thenReturn(Optional.of(TestUtils.getUserNotAdmin()));
        when(transactionStateDao.save(any())).thenReturn(null);
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_PENDING_STATE)).thenReturn(TestUtils.getStateTypePending());
        when(bankAccountService.findByUser(TestUtils.getUserAdmin())).thenReturn(TestUtils.getBankAccount());
        when(merchantDao.findByMerchantKey(any())).thenReturn(TestUtils.getMerchantOptionalIsNotApproved());
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_REFUSED_STATE)).thenReturn(TestUtils.getStateTypeError());
        when(transactionStateDao.save(any())).thenReturn(null);
        when(transactionDao.save(transaction)).thenReturn(null);
        PayTransactionCreatedDto transactionCreatedDto = transactionService.createTransactionFromMerchant(1,TestUtils.getTransactionFromMerchantDto());
        assertEquals(Strings.MERCHANT_IS_NOT_APPROVED, transactionCreatedDto.getStateDescription());
    }
    @Test
    void testCreateTransactionFromMerchantShouldFailWhenUserMerchantIsNotPresent(){
        Transaction transaction = TestUtils.getTransaction();
        TransactionFromMerchantDto transactionFromMerchantDto = TestUtils.getTransactionFromMerchantDto();
        when(transactionDao.save(any())).thenReturn(transaction);
        transaction.setCreateAt(LocalDateTime.now());
        when(userDao.findByIdentifier(transactionFromMerchantDto.getIdentifier())).thenReturn(Optional.of(TestUtils.getUserNotAdmin()));
        when(transactionStateDao.save(any())).thenReturn(null);
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_PENDING_STATE)).thenReturn(TestUtils.getStateTypePending());
        when(merchantDao.findByMerchantKey(any())).thenReturn(TestUtils.getMerchantOptionalIsApproved());
        when(bankAccountService.findByUser(TestUtils.getUserAdmin())).thenReturn(TestUtils.getBankAccount());
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_REFUSED_STATE)).thenReturn(TestUtils.getStateTypeError());
        when(userDao.findById(any())).thenReturn(Optional.empty());
        when(transactionStateDao.save(any())).thenReturn(null);
        when(transactionDao.save(transaction)).thenReturn(null);
        when(modelMapper.map(transaction, TransactionCreatedDto.class)).thenReturn(TestUtils.getTransactionCreatedDto());
        PayTransactionCreatedDto transactionCreatedDto = transactionService.createTransactionFromMerchant(1,TestUtils.getTransactionFromMerchantDto());
        assertEquals(Strings.MERCHANT_BANK_ACCOUNT_NOT_FOUND, transactionCreatedDto.getStateDescription());
    }
    @Test
    void testCreateTransactionFromMerchantShouldFailWhenUserHadNotEnoughBalance(){
        Transaction transaction = TestUtils.getTransaction();
        TransactionFromMerchantDto transactionFromMerchantDto = TestUtils.getTransactionFromMerchantDto();
        when(transactionDao.save(any())).thenReturn(transaction);
        transaction.setCreateAt(LocalDateTime.now());
        when(userDao.findByIdentifier(transactionFromMerchantDto.getIdentifier())).thenReturn(Optional.of(TestUtils.getUserNotAdmin()));
        when(transactionStateDao.save(any())).thenReturn(null);
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_PENDING_STATE)).thenReturn(TestUtils.getStateTypePending());
        when(merchantDao.findByMerchantKey(any())).thenReturn(TestUtils.getMerchantOptionalIsApproved());
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_ERROR_STATE)).thenReturn(TestUtils.getStateTypeError());
        when(userDao.findById(any())).thenReturn(Optional.of(TestUtils.getUserAdmin()));
        when(bankAccountService.findByUser(any())).thenReturn(TestUtils.getBankAccountWhitOutBalance()).thenReturn(TestUtils.getBankAccount2());
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_REFUSED_STATE)).thenReturn(TestUtils.getStateTypeRefused());
        when(transactionStateDao.save(any())).thenReturn(null);
        when(transactionDao.save(transaction)).thenReturn(null);
        when(modelMapper.map(transaction, TransactionCreatedDto.class)).thenReturn(TestUtils.getTransactionCreatedDto());
        when(transactionValidations.validateExternalTransaction(anyInt(),anyInt(),anyString(),any(),any(),any())).thenReturn("NOT NULL");
        PayTransactionCreatedDto transactionCreatedDto = transactionService.createTransactionFromMerchant(1,TestUtils.getTransactionFromMerchantDto());
        assertNotNull(transactionCreatedDto);
    }
    @Test
    void testCreateTransactionFromMerchantShouldSFailWhenTransactionError() {
        Transaction transaction = TestUtils.getTransaction();
        TransactionFromMerchantDto transactionFromMerchantDto = TestUtils.getTransactionFromMerchantDto();
        when(transactionDao.save(any())).thenReturn(transaction);
        transaction.setCreateAt(LocalDateTime.now());
        when(userDao.findByIdentifier(transactionFromMerchantDto.getIdentifier())).thenReturn(Optional.of(TestUtils.getUserNotAdmin()));
        when(transactionStateDao.save(any())).thenReturn(null);
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_PENDING_STATE)).thenReturn(TestUtils.getStateTypePending());
        when(merchantDao.findByMerchantKey(any())).thenReturn(TestUtils.getMerchantOptionalIsApproved());
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_ERROR_STATE)).thenReturn(TestUtils.getStateTypeError());
        when(userDao.findById(any())).thenReturn(Optional.of(TestUtils.getUserAdmin()));
        when(bankAccountService.findByUser(any())).thenReturn(TestUtils.getBankAccountWhitOutBalance()).thenReturn(TestUtils.getBankAccount2());
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_FAILED_STATE)).thenReturn(TestUtils.getStateTypeFailed());
        when(entityManager.getReference(StateType.class, Strings.TRANSACTION_REFUSED_STATE)).thenReturn(TestUtils.getStateTypeRefused());
        when(transactionDao.save(transaction)).thenReturn(null);
        when(modelMapper.map(transaction, TransactionCreatedDto.class)).thenReturn(TestUtils.getTransactionCreatedDto());
        PayTransactionCreatedDto transactionCreatedDto = transactionService.createTransactionFromMerchant(1,TestUtils.getTransactionFromMerchantDto());
        assertNotNull(transactionCreatedDto);
    }
}