package com.endava.endabank.dao;

import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface BankAccountDao extends JpaRepository<BankAccount, Integer> {
    Optional<BankAccount> findByUser(User user);
    Optional<BankAccount> findByAccountNumber(BigInteger account);

    @Query(value = "select id, password, balance, account_number, account_type_id, user_id "+
            "from bank_accounts where user_id = ?1 ", nativeQuery = true)
    BankAccount findByUserId(Integer user_id);
}
