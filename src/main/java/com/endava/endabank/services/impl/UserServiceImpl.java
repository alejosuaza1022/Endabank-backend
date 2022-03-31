package com.endava.endabank.services.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.UserDao;
import com.endava.endabank.dto.user.UserPrincipalSecurity;
import com.endava.endabank.dto.user.UserRegisterDto;
import com.endava.endabank.dto.user.UserRegisterGetDto;
import com.endava.endabank.exceptions.customExceptions.ResourceNotFoundException;
import com.endava.endabank.models.IdentifierType;
import com.endava.endabank.models.Permission;
import com.endava.endabank.models.Role;
import com.endava.endabank.models.User;
import com.endava.endabank.services.IdentifierTypeService;
import com.endava.endabank.services.RoleService;
import com.endava.endabank.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public User findById(Integer id) {
        return userDao.findById(id).
                orElseThrow(() -> new UsernameNotFoundException(Strings.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public UserRegisterGetDto save(UserRegisterDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        Role role = roleService.findById(userDto.getRoleId()).
                orElseThrow(() -> new ResourceNotFoundException(Strings.ROLE_NOT_FOUND));
        IdentifierType identifierType = identifierTypeService.findById(userDto.getTypeIdentifierId()).
                orElseThrow((() -> new ResourceNotFoundException(Strings.IDENTIFIER_TYPE_NOT_FOUND)));
        user.setRole(role);
        user.setIdentifierType(identifierType);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
        return modelMapper.map(user, UserRegisterGetDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UsernamePasswordAuthenticationToken getUsernamePasswordToken(Integer userId) {
        User user = this.findById(userId);
        Role role = user.getRole();
        Set<Permission> permissions = role.getPermissions();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));
        UserPrincipalSecurity userPrincipalSecurity = modelMapper.map(user, UserPrincipalSecurity.class);
        return
                new UsernamePasswordAuthenticationToken(userPrincipalSecurity, null, authorities);
    }


}
