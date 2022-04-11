package com.endava.endabank.service;

import com.endava.endabank.dto.user.UpdatePasswordDto;
import com.endava.endabank.dto.user.UserDetailsDto;
import com.endava.endabank.dto.user.UserPrincipalSecurity;
import com.endava.endabank.dto.user.UserRegisterDto;
import com.endava.endabank.dto.user.UserRegisterGetDto;
import com.endava.endabank.dto.user.UserToApproveAccountDto;
import com.endava.endabank.exceptions.customExceptions.ActionNotAllowedException;
import com.endava.endabank.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;
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

    UserDetailsDto getUserDetails(UserPrincipalSecurity user, Collection<GrantedAuthority> authorities);
}
