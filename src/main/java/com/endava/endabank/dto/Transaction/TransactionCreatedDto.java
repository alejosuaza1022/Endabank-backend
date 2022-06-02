package com.endava.endabank.dto.Transaction;

import com.endava.endabank.dto.BankAccountDto;
import com.endava.endabank.dto.BankAccountMinimalDto;
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
}
