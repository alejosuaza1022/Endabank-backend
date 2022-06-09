package com.endava.endabank.service.impl;

import com.endava.endabank.dao.AccountTypeDao;
import com.endava.endabank.exceptions.custom.ResourceNotFoundException;
import com.endava.endabank.model.AccountType;
import com.endava.endabank.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountTypeServiceImplTest {

    @Mock
    private AccountTypeDao accountTypeDao;

    @InjectMocks
    private AccountTypeServiceImpl accountTypeService;

    @Test
    void testFindByIdShouldFailWhenIdentifierTypeNotFound() {
        AccountType accountType = TestUtils.getAccountType();
        Integer id = accountType.getId();
        when(accountTypeDao.findById(accountType.getId())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> accountTypeService.findById(id));
    }

    @Test
    void testFindByIdShouldSuccessWhenDataCorrect() {
        AccountType accountType = TestUtils.getAccountType();
        Integer id = accountType.getId();
        when(accountTypeDao.findById(accountType.getId())).thenReturn(Optional.of(accountType));
        AccountType accountType1 = accountTypeService.findById(id);
        assertEquals(accountType, accountType1);
    }
}