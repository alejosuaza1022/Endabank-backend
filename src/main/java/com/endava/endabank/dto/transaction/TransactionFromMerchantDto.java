package com.endava.endabank.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFromMerchantDto {

    @NotNull
    private String merchantKey;

    @NotNull
    private String apiId;

    @NotNull
    private String identifier;

    @NotNull
    private Double amount;

    private String description;

    @Size(min = 6, max = 15)
    private String address;
}
