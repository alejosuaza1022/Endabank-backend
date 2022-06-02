package com.endava.endabank.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BankAccountUtils {

    public static String genereteRamdomNumber(Integer len){
        char [] chars = "0123456789".toCharArray();
        int charsLength = chars.length;
        SecureRandom random = new SecureRandom ();
        StringBuilder buffer = new StringBuilder();
        for (int i=0;i<len;i++){
            buffer.append(chars[random.nextInt(charsLength)]);
        }
        return buffer.toString();
    }
}
