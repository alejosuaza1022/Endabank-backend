package com.endava.endabank.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class BankAccountUtilsTest {

    @Test
    void testGenereteRamdomNumberShouldSuccessWhenDataCorrect() {
        String number = BankAccountUtils.genereteRamdomNumber(4);
        assertEquals(number.length(),4);
    }

}