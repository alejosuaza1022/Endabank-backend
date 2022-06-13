package com.endava.endabank.dto.bankaccount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDto {
    private BigInteger accountNumber;
    private Double balance;
    private Integer id;
}
