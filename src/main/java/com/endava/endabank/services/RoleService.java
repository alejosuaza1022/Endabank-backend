package com.endava.endabank.services;

import com.endava.endabank.models.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findById(Integer id);
}
