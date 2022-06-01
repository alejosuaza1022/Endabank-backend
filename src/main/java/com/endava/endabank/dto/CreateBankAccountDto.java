package com.endava.endabank.dto;

import com.endava.endabank.model.AccountType;
import com.endava.endabank.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBankAccountDto {
    private Integer id;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private Float balance;

    @NotNull
    @NotBlank
    private String accountNumber;

    @NotNull
    @NotBlank
    private AccountType accountType;

    @NotNull
    @NotBlank
    private User user;
}
