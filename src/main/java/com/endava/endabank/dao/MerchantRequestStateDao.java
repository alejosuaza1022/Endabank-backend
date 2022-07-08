package com.endava.endabank.dao;

import com.endava.endabank.model.MerchantRequestState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRequestStateDao extends JpaRepository<MerchantRequestState,Integer> {
}
