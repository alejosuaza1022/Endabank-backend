package com.endava.endabank.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private String description;
    private String amount;
    /*private Boolean receiver;*/
    private LocalDateTime createAt;
    private Integer id;
}
