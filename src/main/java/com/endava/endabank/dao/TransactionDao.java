package com.endava.endabank.dao;

import com.endava.endabank.dto.TransactionDto;
import com.endava.endabank.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionDao extends JpaRepository<Transaction, Integer> {
    @Query(value = "select case when bank_account_receiver_id = ?1 then true else false end as wasReceived , amount," +
            " description, create_at createAt, state_type_id stateTypeId, id from transactions" +
            " where state_type_id  = 1 and (bank_account_issuer_id = ?1 or bank_account_receiver_id = ?1 )",
            countQuery = "select count(*) from transactions where state_type_id  = 1 and (bank_account_issuer_id = ?1 " +
                    "or bank_account_receiver_id = ?1 )",nativeQuery = true)
    
    Page<TransactionDto> getListTransactionsSummary(Integer id, Pageable pageable);


}
