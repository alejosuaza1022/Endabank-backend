package com.endava.endabank.dao;

import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountDao extends JpaRepository<BankAccount, Integer> {
    Optional<BankAccount> findByUser(User user);
    Optional<BankAccount> findByAccountNumber(String account);

}
