package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Permissions;
import com.endava.endabank.dao.ForgotUserPasswordTokenDao;
import com.endava.endabank.dao.IdentifierTypeDao;
import com.endava.endabank.dao.RoleDao;
import com.endava.endabank.dao.UserDao;
import com.endava.endabank.dto.user.UserRegisterDto;
import com.endava.endabank.dto.user.UserRegisterGetDto;
import com.endava.endabank.exceptions.customExceptions.UniqueConstraintViolationException;
import com.endava.endabank.model.User;
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
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private ForgotUserPasswordTokenServiceImpl forgotUserPasswordTokenService
            = new ForgotUserPasswordTokenServiceImpl(forgotUserPasswordTokenDao);

    @InjectMocks
    private UserServiceImpl userService =
            new UserServiceImpl(userDao, modelMapper, identifierTypeService, roleService,
                    passwordEncoder, forgotUserPasswordTokenService
            );


    @Nested
    @DisplayName("User save test")
    class SaveUserTests {
        UserRegisterDto userRegisterDto;

        @BeforeEach
        void setUp() {
            userRegisterDto = TestUtils.getUserRegisterDto();
            when(modelMapper.map(userRegisterDto, User.class)).thenReturn(TestUtils.getUserNotAdmin());
        }

        @Test
        void saveShouldThrowExceptionWhenUserEmailAlready() {
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
            when(modelMapper.map(TestUtils.getUserNotAdmin(), UserRegisterGetDto.class)).
                    thenReturn(TestUtils.userRegisterGetDto());
            UserRegisterGetDto userRegisterGetDto = userService.save(userRegisterDto);
            assertEquals(userRegisterGetDto.getEmail(), userRegisterDto.getEmail());
            assertEquals(userRegisterGetDto.getRole().getId(), Permissions.ROLE_USER);
            assertEquals(userRegisterGetDto.getIdentifier(), userRegisterDto.getIdentifier());
            assertEquals(userRegisterGetDto.getTypeIdentifier().getId(), userRegisterDto.getTypeIdentifierId());

        }
    }


}