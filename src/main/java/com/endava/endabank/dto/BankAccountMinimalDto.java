package com.endava.endabank.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankAccountMinimalDto {
    private String accountNumber;
    private Integer id;
}
