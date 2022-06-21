package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.MerchantDao;
import com.endava.endabank.dao.TransactionDao;
import com.endava.endabank.dao.UserDao;
import com.endava.endabank.dto.transaction.TransactionCreateDto;
import com.endava.endabank.dto.transaction.TransactionCreatedDto;
import com.endava.endabank.dto.transaction.TransactionFromMerchantDto;
import com.endava.endabank.exceptions.custom.ResourceNotFoundException;
import com.endava.endabank.model.*;
import com.endava.endabank.service.*;
import com.endava.endabank.utils.transaction.TransactionValidations;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private BankAccountService bankAccountService;
    private TransactionDao transactionDao;
    private ModelMapper modelMapper;
    private EntityManager entityManager;
    private TransactionValidations transactionValidations;
    private MerchantDao merchantDao;
    private UserDao userDao;
    private TransactionStateService transactionStateService;

    @Transactional
    @Override
    public TransactionCreatedDto createTransaction(Integer userId, TransactionCreateDto transactionCreateDto) {
        BankAccount bankAccountIssuer = bankAccountService.
                findByAccountNumber(transactionCreateDto.getBankAccountNumberIssuer());
        BankAccount bankAccountReceiver = bankAccountService.
                findByAccountNumber(transactionCreateDto.getBankAccountNumberReceiver());
        transactionValidations.validateTransaction(userId, bankAccountIssuer, bankAccountReceiver,transactionCreateDto);
        TransactionType transactionType = TransactionType.builder().id(1).build();
        Transaction transaction = Transaction.
                builder().amount(transactionCreateDto.getAmount()).
                description(transactionCreateDto.getDescription()).
                address(transactionCreateDto.getAddress()).
                transactionType(transactionType).bankAccountIssuer(bankAccountIssuer).
                bankAccountReceiver(bankAccountReceiver).build();
        setStateAndBalanceOfTransaction(transaction, bankAccountIssuer, bankAccountReceiver,
                transactionCreateDto.getAmount());
        transaction = transactionDao.save(transaction);
        return modelMapper.map(transaction, TransactionCreatedDto.class);
    }

    @Override
    public Transaction setStateAndBalanceOfTransaction(Transaction transaction,
                                                BankAccount bankAccountIssuer,
                                                BankAccount bankAccountReceiver, Double amount) {
        StateType stateType = entityManager.getReference(StateType.class, Strings.TRANSACTION_APPROVED_STATE);
        if (amount > bankAccountIssuer.getBalance()) {
            stateType = entityManager.getReference(StateType.class, Strings.TRANSACTION_FAILED_STATE);
            transaction.setStateType(stateType);
            transaction.setStateDescription(Strings.NOT_FOUNDS_ENOUGH);
            return transaction;
        }
        bankAccountService.reduceBalance(bankAccountIssuer, amount);
        bankAccountService.increaseBalance(bankAccountReceiver, amount);
        transaction.setStateType(stateType);
        transaction.setStateDescription(Strings.TRANSACTION_COMPLETED);
        return transaction;
    }

    @Override
    public TransactionCreatedDto createTransactionFromMerchant(Integer userId,TransactionFromMerchantDto transactionFromMerchantDto){
        TransactionType transactionType = TransactionType.builder().id(1).build();
        StateType stateType = entityManager.getReference(StateType.class, Strings.TRANSACTION_PENDING_STATE);
        Transaction transaction = Transaction.builder()
                .amount(transactionFromMerchantDto.getAmount())
                .description(transactionFromMerchantDto.getDescription())
                .address(transactionFromMerchantDto.getAddress())
                .transactionType(transactionType)
                .stateType(stateType)
                .stateDescription(Strings.TRANSACTION_CREATED_AND_PENDING)
                .build();
        transaction = transactionDao.save(transaction);
        transactionStateService.saveTransactionState(transaction,stateType,Strings.TRANSACTION_CREATED_AND_PENDING);

        Optional<User> userOptional = userDao.findByIdentifier(transactionFromMerchantDto.getIdentifier());
        User user;
        BankAccount bankAccountIssuer;
        String description;
        if(userOptional.isEmpty()){
            stateType = entityManager.getReference(StateType.class, Strings.TRANSACTION_REFUSED_STATE);
            description = Strings.USER_NOT_FOUND;
            transactionStateService.saveTransactionState(transaction,stateType,
                    description);
            transaction.setStateType(stateType);
            transaction.setStateDescription(description);
            transactionDao.save(transaction);
            return modelMapper.map(transaction, TransactionCreatedDto.class);
        }else{
            user = userOptional.get();
            bankAccountIssuer = bankAccountService.findByUser(user);
            transaction.setBankAccountIssuer(bankAccountIssuer);
        }

        Optional<Merchant> merchantOptional = merchantDao.findByMerchantKey(transactionFromMerchantDto.getMerchantKey());
        BankAccount bankAccountReceiver;
        String apiId;
        stateType = entityManager.getReference(StateType.class, Strings.TRANSACTION_ERROR_STATE);
        if(merchantOptional.isEmpty()){
            description = Strings.MERCHANT_NOT_FOUND;
            transactionStateService.saveTransactionState(transaction,stateType,
                    description);
            transaction.setStateType(stateType);
            transaction.setStateDescription(description);
            transactionDao.save(transaction);
            return modelMapper.map(transaction, TransactionCreatedDto.class);
        }else{
            Merchant merchant = merchantOptional.get();
            Optional<User> userMerchant = userDao.findById(merchant.getUser().getId());
            if(userMerchant.isPresent()) {
                bankAccountReceiver = bankAccountService.findByUser(userMerchant.get());
                apiId = merchant.getApiId();
                transaction.setBankAccountReceiver(bankAccountReceiver);
            }else {
                description = Strings.MERCHANT_BANK_ACCOUNT_NOT_FOUND;
                transactionStateService.saveTransactionState(transaction,stateType,
                        description);
                transaction.setStateType(stateType);
                transaction.setStateDescription(description);
                transactionDao.save(transaction);
                return modelMapper.map(transaction, TransactionCreatedDto.class);
            }
        }

        String validation = transactionValidations.
                validateExternalTransaction(userId,user.getId(),apiId,bankAccountIssuer, bankAccountReceiver,transactionFromMerchantDto);
        if (validation != null){
            stateType = entityManager.getReference(StateType.class, Strings.TRANSACTION_REFUSED_STATE);
            description = validation;
            transactionStateService.saveTransactionState(transaction,stateType,
                    description);
            transaction.setStateType(stateType);
            transaction.setStateDescription(description);
            transactionDao.save(transaction);
            return modelMapper.map(transaction, TransactionCreatedDto.class);
        }

        transaction = setStateAndBalanceOfTransaction(transaction, bankAccountIssuer, bankAccountReceiver,
                transactionFromMerchantDto.getAmount());
        if(transaction.getStateType().getId().equals(Strings.TRANSACTION_APPROVED_STATE)){
            stateType = entityManager.getReference(StateType.class, Strings.TRANSACTION_AUTHORISED_STATE);
            transactionStateService.saveTransactionState(transaction,stateType,
                    Strings.TRANSACTION_COMPLETED);
        } else if (transaction.getStateType().getId().equals(Strings.TRANSACTION_FAILED_STATE)) {
            stateType = entityManager.getReference(StateType.class, Strings.TRANSACTION_REFUSED_STATE);
            transactionStateService.saveTransactionState(transaction,stateType,
                    Strings.NOT_FOUNDS_ENOUGH);
        }
        transaction = transactionDao.save(transaction);
        return modelMapper.map(transaction, TransactionCreatedDto.class);
    }
}
