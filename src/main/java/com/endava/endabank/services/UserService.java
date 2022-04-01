package com.endava.endabank.services;

import com.endava.endabank.dto.user.UpdatePasswordDto;
import com.endava.endabank.dto.user.UserRegisterDto;
import com.endava.endabank.dto.user.UserRegisterGetDto;
import com.endava.endabank.dto.user.UserToApproveAccountDto;
import com.endava.endabank.exceptions.customExceptions.ActionNotAllowedException;
import com.endava.endabank.models.User;
import com.sendgrid.Response;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserToApproveAccountDto> usersToApprove();

    User findById(Integer id);

    UserRegisterGetDto save(UserRegisterDto user);

    UsernamePasswordAuthenticationToken getUsernamePasswordToken(Integer userId);

    UserToApproveAccountDto updateApprove(Integer id , boolean value);

    Map<String, Object> generateResetPassword (String email) ;

    Map<String, String> updateForgotPassword (UpdatePasswordDto updatePasswordDto) throws ActionNotAllowedException;

}
