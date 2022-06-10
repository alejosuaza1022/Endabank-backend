package com.endava.endabank.utils.transaction;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dto.transaction.TransactionCreateDto;
import com.endava.endabank.dto.transaction.TransactionFromMerchantDto;
import com.endava.endabank.exceptions.custom.BadDataException;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.Merchant;
import com.endava.endabank.model.User;
import com.google.common.annotations.VisibleForTesting;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class TransactionValidations {

    public void validateTransaction(Integer userId, BankAccount bankAccountIssuer,
                                    BankAccount bankAccountReceiver,
                                    TransactionCreateDto transactionCreateDto) {
        validateOwner(userId, bankAccountIssuer);
        validateAmount(transactionCreateDto.getAmount());
        validateSameAccount(bankAccountIssuer, bankAccountReceiver);
    }

    @VisibleForTesting
     void validateOwner(Integer userId, BankAccount bankAccountIssuer) {
        if (!Objects.equals(userId, bankAccountIssuer.getUser().getId())) {
            throw new BadDataException(Strings.USER_NOT_OWN_BANK_ACCOUNT);
        }
    }

    @VisibleForTesting
    void validateAmount(Double amount) {
        if (amount <= 0) {
            throw new BadDataException(Strings.AMOUNT_NOT_VALID);
        }
    }

    @VisibleForTesting
    void validateSameAccount(BankAccount bankAccountIssuer, BankAccount bankAccountReciver) {
        if (Objects.equals(bankAccountReciver.getAccountNumber(), bankAccountIssuer.getAccountNumber())){
            throw new BadDataException(Strings.TRANSACTION_SAME_ACCOUNT);
        }
    }

    public void validateExternalTransaction(Integer userId, Integer userIdBd, String apiId,TransactionFromMerchantDto transferFromMerchantDto){
        validateUser(userId,userIdBd);
        validateMerchantApiId(apiId,transferFromMerchantDto.getApiId());
    }

    void validateMerchantApiId(String apiIdDb, String apiId){
        if (!Objects.equals(apiIdDb, apiId)){
            throw new BadDataException(Strings.STATUS_ERROR +": "+ Strings.BAD_API_ID);
        }
    }

    void validateUser(Integer userId, Integer userIdBd){
        if (!Objects.equals(userId, userIdBd)){
            throw new BadDataException(Strings.STATUS_FRAUD +": "+ Strings.BAD_USER_DATA);
        }
    }
}
