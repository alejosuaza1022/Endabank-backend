package com.endava.endabank.dao;

import com.endava.endabank.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountDao extends JpaRepository<BankAccount, Integer> {
    Optional<BankAccount> findByUserId(Integer id);
}
