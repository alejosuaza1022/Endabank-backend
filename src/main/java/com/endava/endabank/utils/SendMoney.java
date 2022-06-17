package com.endava.endabank.utils;

import org.springframework.core.SpringVersion;

public class SendMoney {

    private String amount;
    private String bankAccountNumberIssuer;
    private String bankAccountNumberReceiver;
    private String description;
    private String address;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBankAccountNumberIssuer() {
        return bankAccountNumberIssuer;
    }

    public void setBankAccountNumberIssuer(String bankAccountNumberIssuer) {
        this.bankAccountNumberIssuer = bankAccountNumberIssuer;
    }

    public String getBankAccountNumberReceiver() {
        return bankAccountNumberReceiver;
    }

    public void setBankAccountNumberReceiver(String bankAccountNumberReceiver) {
        this.bankAccountNumberReceiver = bankAccountNumberReceiver;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
