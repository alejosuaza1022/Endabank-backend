package com.endava.endabank.dto;

import java.time.LocalDateTime;


public interface TransactionDto {
    String getDescription();
    String getAmount();
    Boolean getWasReceived();
    String getCreateAt();
    Integer getStateTypeId();
    Integer getId();
}
