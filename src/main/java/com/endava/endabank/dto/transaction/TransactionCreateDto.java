package com.endava.endabank.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCreateDto {

    @NotNull
    private Double amount;

    @NotNull
    private BigInteger bankAccountNumberIssuer;

    @NotNull
    private BigInteger bankAccountNumberReceiver;

    private String description;

    @Size(min = 6, max = 15)
    private String address;

}
