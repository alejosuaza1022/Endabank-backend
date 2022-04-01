package com.endava.endabank.services.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.ForgotUserPasswordTokenDao;
import com.endava.endabank.models.ForgotUserPasswordToken;
import com.endava.endabank.services.ForgotUserPasswordTokenService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ForgotUserPasswordTokenServiceImpl implements ForgotUserPasswordTokenService {
    private final ForgotUserPasswordTokenDao forgotUserPasswordTokenDao;

    public ForgotUserPasswordTokenServiceImpl(ForgotUserPasswordTokenDao forgotUserPasswordTokenDao) {
        this.forgotUserPasswordTokenDao = forgotUserPasswordTokenDao;
    }

    @Override
    public ForgotUserPasswordToken findByUserId(Integer id) {
        return forgotUserPasswordTokenDao.findByUserId(id).
                orElseThrow(() -> new UsernameNotFoundException(Strings.USER_TOKEN_NOT_FOUND));
    }

    @Override
    public void save(ForgotUserPasswordToken forgotUserPasswordTokenNew) {
        Optional<ForgotUserPasswordToken> forgotUserPasswordTokenDbOpt =
                forgotUserPasswordTokenDao.findByUserId(forgotUserPasswordTokenNew.getUser().getId());
        if (forgotUserPasswordTokenDbOpt.isPresent()) {
            ForgotUserPasswordToken forgotUserPasswordTokenDb = forgotUserPasswordTokenDbOpt.get();
            forgotUserPasswordTokenDb.setToken(forgotUserPasswordTokenNew.getToken());
            forgotUserPasswordTokenNew = forgotUserPasswordTokenDb;
        }
        forgotUserPasswordTokenDao.save(forgotUserPasswordTokenNew);
    }

    @Override
    public void deleteById(Integer id) {
        forgotUserPasswordTokenDao.deleteById(id);
    }
}
