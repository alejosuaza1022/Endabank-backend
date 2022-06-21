package com.endava.endabank.utils.transaction;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dto.transaction.TransactionCreateDto;
import com.endava.endabank.dto.transaction.TransactionFromMerchantDto;
import com.endava.endabank.exceptions.custom.BadDataException;
import com.endava.endabank.model.BankAccount;
import com.google.common.annotations.VisibleForTesting;
import lombok.AllArgsConstructor;
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

    public String validateExternalTransaction(Integer userId, Integer userIdBd, String apiId,
                                            BankAccount bankAccountIssuer,
                                            BankAccount bankAccountReceiver,
                                            TransactionFromMerchantDto transferFromMerchantDto){
        String response = validateUser(userId,userIdBd);
        if(response == null){
            response = validateMerchantApiId(apiId,transferFromMerchantDto.getApiId());
            if(response == null){
                response = validateOwnerM(userId, bankAccountIssuer);
                if(response == null){
                    response = validateAmountM(transferFromMerchantDto.getAmount());
                    if(response == null){
                        response = validateSameAccountM(bankAccountIssuer, bankAccountReceiver);
                    }
                }
            }
        }
        return response;
    }

    String validateMerchantApiId(String apiIdDb, String apiId){
        if (!Objects.equals(apiIdDb, apiId)){
            return (Strings.BAD_API_ID);
        }
        return null;
    }

    String validateUser(Integer userId, Integer userIdBd){
        if (!Objects.equals(userId, userIdBd)){
            return (Strings.BAD_USER_DATA);
        }
        return null;
    }

    @VisibleForTesting
    String validateOwnerM(Integer userId, BankAccount bankAccountIssuer) {
        if (!Objects.equals(userId, bankAccountIssuer.getUser().getId())) {
            return(Strings.USER_NOT_OWN_BANK_ACCOUNT);
        }
        return null;
    }

    @VisibleForTesting
    String validateAmountM(Double amount) {
        if (amount <= 0) {
            return(Strings.AMOUNT_NOT_VALID);
        }
        return null;
    }

    @VisibleForTesting
    String validateSameAccountM(BankAccount bankAccountIssuer, BankAccount bankAccountReciver) {
        if (Objects.equals(bankAccountReciver.getAccountNumber(), bankAccountIssuer.getAccountNumber())){
            return(Strings.TRANSACTION_SAME_ACCOUNT);
        }
        return null;
    }

}
