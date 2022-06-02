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
    private String password;

    @NotNull
    private Double balance;

    @NotBlank
    private String accountNumber;

    @NotNull
    private AccountType accountType;

    @NotNull
    private User user;
}
