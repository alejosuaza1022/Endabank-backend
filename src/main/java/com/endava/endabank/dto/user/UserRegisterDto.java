package com.endava.endabank.dto.user;


import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserRegisterDto {
    private Integer id;
    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min = 10,max = 20)
    private String phoneNumber;

    @NotNull
    @Size(min = 6,max = 20)
    private String identifier;

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @Size(min = 8, max = 20)
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,20})",
            message = "1 Capital, 1 Special character, 1 Number and 8 to 20 digits,")
    private String password;

    @NotNull
    @Digits(fraction = 0,integer = 2, message = "Number between 1 and 99")
    private Integer typeIdentifierId;

    public void setEmail(String email){
        this.email = email.toLowerCase();
    }
}
