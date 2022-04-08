package com.endava.endabank.service.impl;

import com.endava.endabank.dao.UserDao;
import com.endava.endabank.security.utils.JwtManage;
import com.endava.endabank.service.ForgotUserPasswordTokenService;
import com.endava.endabank.service.IdentifierTypeService;
import com.endava.endabank.service.RoleService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;


class UserServiceImplTest {

    @Mock
    private UserDao userDao;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private IdentifierTypeService identifierTypeService;
    @InjectMocks
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtManage jwtManage;
    @InjectMocks
    private ForgotUserPasswordTokenService forgotUserPasswordTokenService;

    private UserServiceImpl userService;


    @Test
    void save() {
    }
}