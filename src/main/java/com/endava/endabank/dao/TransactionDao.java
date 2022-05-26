package com.endava.endabank.dao;

import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionDao extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllByBankAccountIssuerOrBankAccountReceiver(BankAccount bankAccountIssuer, BankAccount bankAccountReceiver);
}
