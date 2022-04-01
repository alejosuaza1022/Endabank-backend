package com.endava.endabank.dto.user;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdatePasswordDto {
    private String token;
    private String oldPassword;
    @Size(min = 8, max = 20)
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,20})",
            message = "1 Capital, 1 Special character, 1 Number and 8 to 20 digits,")
    private String password;
    @Size(min = 8, max = 20)
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,20})",
            message = "1 Capital, 1 Special character, 1 Number and 8 to 20 digits,")
    private String rePassword;
}
