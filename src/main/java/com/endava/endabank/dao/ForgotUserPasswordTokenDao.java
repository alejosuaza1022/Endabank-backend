package com.endava.endabank.dao;


import com.endava.endabank.models.ForgotUserPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ForgotUserPasswordTokenDao  extends  JpaRepository<ForgotUserPasswordToken, Integer> {
    Optional<ForgotUserPasswordToken> findByUserId(Integer id);
}
