package com.endava.endabank.service.impl;

import com.endava.endabank.dao.IdentifierTypeDao;
import com.endava.endabank.model.IdentifierType;
import com.endava.endabank.service.IdentifierTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IdentifierTypeServiceImpl implements IdentifierTypeService {
    private final IdentifierTypeDao identifierTypeDao;

    public IdentifierTypeServiceImpl(IdentifierTypeDao identifierTypeDao) {
        this.identifierTypeDao = identifierTypeDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IdentifierType> findById(Integer id) {
        return identifierTypeDao.findById(id);
    }
}
