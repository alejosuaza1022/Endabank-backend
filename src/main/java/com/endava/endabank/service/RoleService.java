package com.endava.endabank.service;

import com.endava.endabank.model.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findById(Integer id);
}
