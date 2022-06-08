package com.endava.endabank.dto.transaction;

import com.endava.endabank.dto.bankaccount.BankAccountDto;
import com.endava.endabank.dto.bankaccount.BankAccountMinimalDto;
import com.endava.endabank.dto.StateTypeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCreatedDto {
    private Double amount;
    private StateTypeDto stateType;
    private String stateDescription;
    private BankAccountMinimalDto bankAccountReceiver;
    private BankAccountDto bankAccountIssuer;
    private Integer id;
    private String createAt;
}
