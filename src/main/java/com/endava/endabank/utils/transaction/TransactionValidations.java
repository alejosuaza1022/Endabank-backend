package com.endava.endabank.utils.transaction;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dto.Transaction.TransactionCreateDto;
import com.endava.endabank.exceptions.custom.BadDataException;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.StateType;
import com.endava.endabank.model.Transaction;
import com.google.common.annotations.VisibleForTesting;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Objects;

@Component
@AllArgsConstructor
public class TransactionValidations {

    public void validateTransaction(Integer userId, BankAccount bankAccountIssuer, TransactionCreateDto transactionCreateDto) {
        validateOwner(userId, bankAccountIssuer);
        validateAmount(transactionCreateDto.getAmount());
    }

    @VisibleForTesting
    private void validateOwner(Integer userId, BankAccount bankAccountIssuer) {
        if (!Objects.equals(userId, bankAccountIssuer.getUser().getId())) {
            throw new BadDataException(Strings.USER_NOT_OWN_BANK_ACCOUNT);
        }
    }
    @VisibleForTesting
    private void validateAmount(Double amount) {
        if (amount <= 0) {
            throw new BadDataException(Strings.AMOUNT_NOT_VALID);
        }
    }
}
