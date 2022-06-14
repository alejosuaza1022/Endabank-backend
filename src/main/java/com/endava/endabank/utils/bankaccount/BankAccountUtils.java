package com.endava.endabank.utils.bankaccount;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.BankAccountDao;
import com.endava.endabank.exceptions.custom.CustomAuthenticationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.security.SecureRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BankAccountUtils {

    public static String generateRandomNumber(Integer len){
        char [] chars = "0123456789".toCharArray();
        int charsLength = chars.length;
        SecureRandom random = new SecureRandom ();
        StringBuilder buffer = new StringBuilder();
        for (int i=0;i<len;i++){
            buffer.append(chars[random.nextInt(charsLength)]);
        }
        return buffer.toString();
    }

    public static BigInteger validateAccountNumber(BigInteger account, BankAccountDao bankAccountDao) {
        BigInteger comp = BigInteger.valueOf(Long.parseLong("1000000000000000"));
        while(bankAccountDao.findByAccountNumber(account).isPresent() || account.compareTo(comp) < 0){
            account = BigInteger.valueOf(Long.parseLong(BankAccountUtils.generateRandomNumber(16)));
        }
        return account;
    }
    public static void validateBankAccountEmail(String emailToken, String email) {
        if (!emailToken.equals(email)){
            throw new CustomAuthenticationException(Strings.EMAIL_NOT_MATCH);
        }
    }
}
