package com.endava.endabank.service;

import com.endava.endabank.model.IdentifierType;

import java.util.Optional;

public interface IdentifierTypeService {
    Optional<IdentifierType> findById(Integer id);
}
