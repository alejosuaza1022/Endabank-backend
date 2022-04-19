package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Permissions;
import com.endava.endabank.dao.ForgotUserPasswordTokenDao;
import com.endava.endabank.dao.IdentifierTypeDao;
import com.endava.endabank.dao.RoleDao;
import com.endava.endabank.dao.UserDao;
import com.endava.endabank.dto.user.UpdatePasswordDto;
import com.endava.endabank.dto.user.UserPrincipalSecurity;
import com.endava.endabank.dto.user.UserRegisterDto;
import com.endava.endabank.exceptions.customexceptions.UniqueConstraintViolationException;
import com.endava.endabank.model.User;
import com.endava.endabank.security.utils.JwtManage;
import com.endava.endabank.service.ForgotUserPasswordTokenService;
import com.endava.endabank.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private IdentifierTypeDao identifierTypeDao;

    @InjectMocks
    private IdentifierTypeServiceImpl identifierTypeService
            = new IdentifierTypeServiceImpl(identifierTypeDao);

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private RoleServiceImpl roleService = new RoleServiceImpl(roleDao);

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ForgotUserPasswordTokenDao forgotUserPasswordTokenDao;

    @InjectMocks
    private ForgotUserPasswordTokenServiceImpl forgotUserPasswordTokenServiceImpl
            = new ForgotUserPasswordTokenServiceImpl(forgotUserPasswordTokenDao);

    @InjectMocks
    private UserServiceImpl userService =
            new UserServiceImpl(userDao, modelMapper, identifierTypeService, roleService,
                    passwordEncoder, forgotUserPasswordTokenServiceImpl
            );

    @Mock
    private ForgotUserPasswordTokenService forgotUserPasswordTokenService;

    @Nested
    @DisplayName("User save test" )
    class SaveUserTests {
        UserRegisterDto userRegisterDto;

        @BeforeEach
        void setUp() {
            userRegisterDto = TestUtils.getUserRegisterDto();
            when(modelMapper.map(userRegisterDto, User.class)).thenReturn(TestUtils.getUserNotAdmin());
        }


        @Test
        void saveShouldThrowExceptionWhenUserEmailAlreadyExists() {
            when(userDao.findByEmail(userRegisterDto.getEmail())).thenReturn(Optional.of(TestUtils.getUserNotAdmin()));
            assertThrows(UniqueConstraintViolationException.class, () -> userService.save(userRegisterDto));
        }

        @Test
        void saveShouldThrowExceptionWhenUserIdentifierAlreadyExists() {
            when(userDao.findByEmail(userRegisterDto.getEmail())).thenReturn(Optional.empty());
            when(userDao.findByIdentifier(userRegisterDto.getIdentifier())).thenReturn(Optional.of(TestUtils.getUserNotAdmin()));
            assertThrows(UniqueConstraintViolationException.class, () -> userService.save(userRegisterDto));
        }

        @Test
        void saveUserShouldSuccess() {
            when(userDao.findByEmail(userRegisterDto.getEmail())).thenReturn(Optional.empty());
            when(userDao.findByIdentifier(userRegisterDto.getIdentifier())).thenReturn(Optional.empty());
            when(roleService.findById(Permissions.ROLE_USER)).
                    thenReturn(Optional.of(TestUtils.userRole()));
            when(identifierTypeService.findById(userRegisterDto.
                    getTypeIdentifierId())).thenReturn(Optional.of(TestUtils.identifierTypeCC()));
            User userDb = userService.save(userRegisterDto);
            assertEquals(userDb.getEmail(), userRegisterDto.getEmail());
            assertEquals(Permissions.ROLE_USER, userDb.getRole().getId());
            assertEquals(userDb.getIdentifier(), userRegisterDto.getIdentifier());
            assertEquals(userDb.getIdentifierType().getId(), userRegisterDto.getTypeIdentifierId());
        }
    }
    @Test
    void updateForgotPasswordShouldSuccess() {
        UpdatePasswordDto updatePasswordDto = TestUtils.getUpdatePasswordDto();
        User user = TestUtils.getUserNotAdmin();
        String secret_dummy = "ZHVtbXkgdmFsdWUK";
        String token = JwtManage.generateToken(user.getId(), user.getEmail(), secret_dummy);
        when(forgotUserPasswordTokenService.findByUserId(user.getId())).thenReturn(TestUtils.getForgotUserPasswordToken(token));
        when(userDao.findById(user.getId())).thenReturn(Optional.of(TestUtils.getUserNotAdmin()));
        updatePasswordDto.setToken(token);
        Map<String, String> map = userService.updateForgotPassword(updatePasswordDto);
        assertEquals(map.get("message"), "Password update");
    }

    @Test
    void updateForgotPasswordShouldThrowException() {
        UpdatePasswordDto updatePasswordDto = TestUtils.getUpdatePasswordDto();
        User user = TestUtils.getUserNotAdmin();
        String secret_dummy = "ZHVtbXkgdmFsdWUK";
        String token = JwtManage.generateToken(user.getId(), user.getEmail(), secret_dummy);
        when(forgotUserPasswordTokenService.findByUserId(user.getId())).thenReturn(TestUtils.getForgotUserPasswordToken(token+"abc"));
        updatePasswordDto.setToken(token);
        assertThrows(AccessDeniedException.class, () -> userService.updateForgotPassword(updatePasswordDto));
    }

    @Test
    void updatePasswordShouldSuccess() {
        UserPrincipalSecurity userPrincipalSecurity = TestUtils.getUserPrincipalSecurity();
        UpdatePasswordDto updatePasswordDto = TestUtils.getUpdatePasswordDto();
        when(userDao.findById(userPrincipalSecurity.getId())).thenReturn(Optional.of(TestUtils.getUserNotAdmin()));
        when(passwordEncoder.matches(updatePasswordDto.getOldPassword(), TestUtils.getUserNotAdmin().getPassword())).thenReturn(true);
        Map<String, String> map = userService.updatePassword(userPrincipalSecurity,updatePasswordDto);
        assertEquals(map.get("message"), "Password update");
    }

    @Test
    void updatePasswordShouldThrowException(){
        UserPrincipalSecurity userPrincipalSecurity = TestUtils.getUserPrincipalSecurity();
        UpdatePasswordDto updatePasswordDto = TestUtils.getUpdatePasswordDto();
        when(userDao.findById(userPrincipalSecurity.getId())).thenReturn(Optional.of(TestUtils.getUserNotAdmin()));
        assertThrows(AccessDeniedException.class, () -> userService.updatePassword(userPrincipalSecurity,updatePasswordDto));
    }

}

