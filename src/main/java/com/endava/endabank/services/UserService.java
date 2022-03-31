package com.endava.endabank.services;

import com.endava.endabank.dto.user.UserRegisterDto;
import com.endava.endabank.dto.user.UserRegisterGetDto;
import com.endava.endabank.models.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    User findById(Integer id);

    UserRegisterGetDto save(UserRegisterDto user);

    UsernamePasswordAuthenticationToken getUsernamePasswordToken(Integer userId);

}
