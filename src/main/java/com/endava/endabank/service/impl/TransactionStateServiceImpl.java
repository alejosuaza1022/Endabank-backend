package com.endava.endabank.service.impl;

import com.endava.endabank.dao.TransactionStateDao;
import com.endava.endabank.model.StateType;
import com.endava.endabank.model.Transaction;
import com.endava.endabank.model.TransactionState;
import com.endava.endabank.service.TransactionStateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionStateServiceImpl implements TransactionStateService {

    private TransactionStateDao transactionStateDao;

    @Override
    public void saveTransactionState(Transaction transaction, StateType stateType, String description) {
        TransactionState transactionState = TransactionState.builder()
                .transaction(transaction)
                .stateType(stateType)
                .description(description).build();
        transactionStateDao.save(transactionState);
    }
}
