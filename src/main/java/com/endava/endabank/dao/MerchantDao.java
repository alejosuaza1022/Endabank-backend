package com.endava.endabank.dao;

import com.endava.endabank.model.Merchant;
import com.endava.endabank.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantDao extends JpaRepository<Merchant,Integer>, JpaSpecificationExecutor<Merchant> {
    Optional<Merchant> findByTaxId(String taxId);

    Optional<Merchant> findByUser(User user);

    Page<Merchant> findAll(Specification<Merchant> spec, Pageable pageable);

    List<Merchant> findAll(Specification<Merchant> spec);

}
