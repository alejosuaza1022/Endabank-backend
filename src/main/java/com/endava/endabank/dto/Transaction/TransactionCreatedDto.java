package com.endava.endabank.dto.Transaction;

import com.endava.endabank.dto.BankAccountDto;
import com.endava.endabank.dto.BankAccountMinimalDto;
import com.endava.endabank.dto.StateTypeDto;
import lombok.Data;

@Data
public class TransactionCreatedDto {
    private Double amount;
    private StateTypeDto stateType;
    private String stateDescription;
    private BankAccountMinimalDto bankAccountReceiver;
    private BankAccountDto bankAccountIssuer;
    private Integer id;
}
