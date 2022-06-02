package com.endava.endabank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountMinimalDto {
    private BigInteger accountNumber;
    private Integer id;
}
