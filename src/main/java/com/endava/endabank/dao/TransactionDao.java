package com.endava.endabank.dao;

import com.endava.endabank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionDao extends JpaRepository<Transaction, Integer> {
}
