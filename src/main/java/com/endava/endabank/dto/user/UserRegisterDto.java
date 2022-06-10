package com.endava.endabank.dto.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    private Integer id;
    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min = 10, max = 20)
    private String phoneNumber;

    @NotNull
    @Size(min = 6, max = 20)
    private String identifier;

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,20})",
            message = "1 Capital, 1 Special character, 1 Number and 8 to 20 digits,")
    private String password;

    @NotNull
    @Digits(fraction = 0, integer = 2, message = "Number between 1 and 99")
    private Integer typeIdentifierId;
}
