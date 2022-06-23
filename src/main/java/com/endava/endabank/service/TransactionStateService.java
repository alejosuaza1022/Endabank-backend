package com.endava.endabank.service;

import com.endava.endabank.model.StateType;
import com.endava.endabank.model.Transaction;

public interface TransactionStateService {
    void saveTransactionState(Transaction transaction, StateType stateType, String description);
}
