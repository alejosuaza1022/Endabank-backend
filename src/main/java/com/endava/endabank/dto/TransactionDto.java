package com.endava.endabank.dto;

import lombok.Data;

@Data
public class TransactionDto {
    private String description;
    private String amount;
    private Integer id;
}
