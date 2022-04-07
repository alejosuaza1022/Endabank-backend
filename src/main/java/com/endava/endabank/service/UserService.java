package com.endava.endabank.service;

import com.endava.endabank.dto.user.*;
import com.endava.endabank.exceptions.customExceptions.ActionNotAllowedException;
import com.endava.endabank.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserToApproveAccountDto> usersToApprove();

    User findById(Integer id);

    UserRegisterGetDto save(UserRegisterDto user);

    UsernamePasswordAuthenticationToken getUsernamePasswordToken(Integer userId);

    UserToApproveAccountDto updateApprove(Integer id, boolean value);

    Map<String, Object> generateResetPassword(String email);

    Map<String, String> updateForgotPassword(UpdatePasswordDto updatePasswordDto) throws ActionNotAllowedException;

    Map<String, String> updatePassword(UserPrincipalSecurity user, UpdatePasswordDto updatePasswordDto) throws ActionNotAllowedException;


}
