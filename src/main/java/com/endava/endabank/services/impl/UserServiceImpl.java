package com.endava.endabank.services.impl;

import com.endava.endabank.constants.Permissions;
import com.endava.endabank.constants.Routes;
import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.UserDao;
import com.endava.endabank.dto.user.*;
import com.endava.endabank.exceptions.customExceptions.ActionNotAllowedException;
import com.endava.endabank.exceptions.customExceptions.ResourceNotFoundException;
import com.endava.endabank.exceptions.customExceptions.UniqueConstraintViolationException;
import com.endava.endabank.models.*;
import com.endava.endabank.security.utils.JwtManage;
import com.endava.endabank.services.ForgotUserPasswordTokenService;
import com.endava.endabank.services.IdentifierTypeService;
import com.endava.endabank.services.RoleService;
import com.endava.endabank.services.UserService;
import com.endava.endabank.utils.MailService;
import com.endava.endabank.utils.user.UserValidations;
import com.sendgrid.Response;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final ModelMapper modelMapper;
    private final IdentifierTypeService identifierTypeService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtManage jwtManage;
    private final ForgotUserPasswordTokenService forgotUserPasswordTokenService;

    @Override
    @Transactional(readOnly = true)
    public List<UserToApproveAccountDto> usersToApprove() {
        List<User> users = userDao.findAll();
        return users.stream().map(this::mapToDto).collect(Collectors.toList());
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
        Optional<User> userExist  = userDao.findByEmail(user.getEmail());
        if(userExist.isPresent()){
            throw new ResourceNotFoundException(Strings.CONSTRAINT_EMAIL_VIOLATED);
        }
        userExist = userDao.findByIdentifier(user.getIdentifier());
        if(userExist.isPresent()){
            throw  new UniqueConstraintViolationException(Strings.CONSTRAINT_IDENTIFIER_VIOLATED);
        }
        Role role = roleService.findById(Permissions.ROLE_USER).
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

    @Override
    public UserToApproveAccountDto updateApprove(Integer id, boolean value) {
        User user = userDao.findById(id).orElseThrow(() ->
                new UsernameNotFoundException(Strings.USER_NOT_FOUND));
        user.setIsApproved(value);
        userDao.save(user);
        return this.mapToDto(user);
    }

    @Override
    public Map<String, Object> generateResetPassword(String email) {
        Map<String, Object> map = new HashMap<>();
        int httpStatus;
        try {
            User user = userDao.findByEmail(email).
                    orElseThrow(() -> new UsernameNotFoundException(Strings.USER_NOT_FOUND));
            String token = jwtManage.generateToken(user.getId(), user.getEmail());
            ForgotUserPasswordToken forgotUserPasswordToken = new ForgotUserPasswordToken(user, token);
            forgotUserPasswordTokenService.save(forgotUserPasswordToken);
            String link = Routes.RESET_PASSWORD_FRONTEND_ROUTE + token;
            Response response = MailService.SendEmail(user.getEmail(), user.getFirstName(), link);
            httpStatus = response.getStatusCode();
            map.put("statusCode", HttpStatus.valueOf(httpStatus));
        } catch (Exception e) {
            httpStatus = HttpStatus.SERVICE_UNAVAILABLE.value();
            System.out.println(e.getMessage());

        }
        map.put("message", httpStatus == HttpStatus.ACCEPTED.value() ? Strings.MAIL_SENT : Strings.MAIL_FAIL);
        map.put("statusCode", HttpStatus.valueOf(httpStatus));
        return map;

    }

    @Override
    @Transactional
    public Map<String, String> updateForgotPassword(UpdatePasswordDto updatePasswordDto) throws ActionNotAllowedException {
        Map<String, String> map = new HashMap<>();
        UserValidations.comparePasswords(updatePasswordDto.getPassword(), updatePasswordDto.getRePassword());
        int  userId = UserValidations.validateUserForgotPasswordToken(
                jwtManage, forgotUserPasswordTokenService, updatePasswordDto.getToken());
        User user = userDao.findById(userId).
                orElseThrow(() -> new UsernameNotFoundException(Strings.USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(updatePasswordDto.getPassword()));
        userDao.save(user);
        map.put("message", Strings.PASSWORD_UPDATED);
        return map;
    }

    @Override
    public Map<String, String> updatePassword(UserPrincipalSecurity userPrincipalSecurity, UpdatePasswordDto updatePasswordDto) throws ActionNotAllowedException {
        Map<String, String> map = new HashMap<>();
        UserValidations.comparePasswords(updatePasswordDto.getPassword(), updatePasswordDto.getRePassword());
        User user = userDao.findById(userPrincipalSecurity.getId()).
                orElseThrow(() -> new UsernameNotFoundException(Strings.USER_NOT_FOUND));
        UserValidations.validateOldPassword(passwordEncoder, user, updatePasswordDto.getOldPassword());
        user.setPassword(passwordEncoder.encode(updatePasswordDto.getPassword()));
        userDao.save(user);
        map.put("message", Strings.PASSWORD_UPDATED);
        return  map;
    }

    private UserToApproveAccountDto mapToDto(User user) {
        return modelMapper.map(user, UserToApproveAccountDto.class);
    }


}
