package com.endava.endabank.dto;


public interface TransactionDto {
    String getDescription();
    String getAmount();
    Boolean getWasReceived();
    String getCreateAt();
    Integer getStateTypeId();
    Integer getId();
}
