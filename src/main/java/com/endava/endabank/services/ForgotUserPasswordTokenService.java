package com.endava.endabank.services;


import com.endava.endabank.models.ForgotUserPasswordToken;

public interface ForgotUserPasswordTokenService {
    ForgotUserPasswordToken findByUserId(Integer id);
    void save(ForgotUserPasswordToken forgotUserPasswordToken);
    void deleteById(Integer id);
}
