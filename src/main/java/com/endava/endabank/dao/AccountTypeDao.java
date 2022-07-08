package com.endava.endabank.dao;

import com.endava.endabank.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeDao extends JpaRepository<AccountType, Integer> {
}
