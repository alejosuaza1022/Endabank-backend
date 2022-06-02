package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.TransactionDao;
import com.endava.endabank.dto.Transaction.TransactionCreateDto;
import com.endava.endabank.dto.Transaction.TransactionCreatedDto;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.StateType;
import com.endava.endabank.model.Transaction;
import com.endava.endabank.model.TransactionType;
import com.endava.endabank.service.BankAccountService;
import com.endava.endabank.service.TransactionService;
import com.endava.endabank.utils.transaction.TransactionValidations;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private BankAccountService bankAccountService;
    private TransactionDao transactionDao;
    private ModelMapper modelMapper;
    private EntityManager entityManager;
    private TransactionValidations transactionValidations;


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

    @Transactional
    @Override
    public void setStateAndBalanceOfTransaction(Transaction transaction,
                                                BankAccount bankAccountIssuer,
                                                BankAccount bankAccountReceiver, Double amount) {
        StateType stateType = entityManager.getReference(StateType.class, Strings.TRANSACTION_APPROVED_STATE);
        if (amount > bankAccountIssuer.getBalance()) {
            stateType = entityManager.getReference(StateType.class, Strings.TRANSACTION_FAILED_STATE);
            transaction.setStateType(stateType);
            transaction.setStateDescription(Strings.NOT_FOUNDS_ENOUGH);
            return;
        }
        bankAccountService.reduceBalance(bankAccountIssuer, amount);
        bankAccountService.increaseBalance(bankAccountReceiver, amount);
        transaction.setStateType(stateType);
        transaction.setStateDescription(Strings.TRANSACTION_COMPLETED);
    }
}
