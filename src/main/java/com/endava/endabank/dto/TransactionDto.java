package com.endava.endabank.dto;


public interface TransactionDto {
    String getDescription();
    Double getAmount();
    Boolean getWasReceived();
    String getCreateAt();
    Integer getStateTypeId();
    Integer getId();
}
