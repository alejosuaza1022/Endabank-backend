package com.endava.endabank.utils.merchant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class MerchantUtilsTest {

    @Test
    void testGenerateRandomKeyShouldSuccessWhenDataCorrect(){
        String key = MerchantUtils.generateRandomKey(10);

        assertEquals(10,key.length());
    }
}
