package com.endava.endabank.services;

import com.endava.endabank.dto.user.UserRegisterDto;
import com.endava.endabank.dto.user.UserRegisterGetDto;
import com.endava.endabank.dto.user.UserToApproveAccountDto;
import com.endava.endabank.models.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface UserService {
    List<UserToApproveAccountDto> usersToApprove();

    User findById(Integer id);

    UserRegisterGetDto save(UserRegisterDto user);

    UsernamePasswordAuthenticationToken getUsernamePasswordToken(Integer userId);

    UserToApproveAccountDto updateApprove(Integer id , boolean value);
}
