package com.endava.endabank.services.impl;

import com.endava.endabank.dao.RoleDao;
import com.endava.endabank.models.Role;
import com.endava.endabank.services.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findById(Integer id) {
        return roleDao.findById(id);
    }
}
