package com.endava.endabank.dao;

import com.endava.endabank.model.TransactionState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionStateDao extends JpaRepository<TransactionState, Integer> {
}
