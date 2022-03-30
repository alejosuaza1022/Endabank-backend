package com.endava.endabank.services.impl;

import com.endava.endabank.dao.UserDao;
import com.endava.endabank.dto.user.UserRegisterDto;
import com.endava.endabank.dto.user.UserRegisterGetDto;
import com.endava.endabank.exceptions.customExceptions.ResourceNotFoundException;
import com.endava.endabank.models.Role;
import com.endava.endabank.models.IdentifierType;
import com.endava.endabank.models.User;
import com.endava.endabank.services.IdentifierTypeService;
import com.endava.endabank.services.RoleService;
import com.endava.endabank.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final ModelMapper modelMapper;
    private final IdentifierTypeService identifierTypeService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, ModelMapper modelMapper,
                           IdentifierTypeService identifierTypeService,
                           RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.modelMapper = modelMapper;
        this.identifierTypeService = identifierTypeService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    @Transactional
    public UserRegisterGetDto save(UserRegisterDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        Role role = roleService.findById(userDto.getRoleId()).
                orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        IdentifierType identifierType = identifierTypeService.findById(userDto.getTypeIdentifierId()).
                orElseThrow((() -> new ResourceNotFoundException("Identifier type not found")));
        user.setRole(role);
        user.setIdentifierType(identifierType);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
        return modelMapper.map(user, UserRegisterGetDto.class);
    }
}
