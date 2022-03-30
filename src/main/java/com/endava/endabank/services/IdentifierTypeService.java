package com.endava.endabank.services;

import com.endava.endabank.models.IdentifierType;

import java.util.Optional;

public interface IdentifierTypeService {
    Optional<IdentifierType> findById(Integer id);
}
