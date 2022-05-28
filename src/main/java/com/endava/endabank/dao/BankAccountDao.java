package com.endava.endabank.dao;

import com.endava.endabank.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountDao extends JpaRepository<BankAccount, Integer> {
}
