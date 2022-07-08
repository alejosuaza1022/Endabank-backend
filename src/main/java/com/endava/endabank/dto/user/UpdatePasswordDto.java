package com.endava.endabank.dto.user;

import com.endava.endabank.constants.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordDto {
    private String token;
    private String oldPassword;
    @Size(min = 8, max = 20)
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,20})",
            message = Strings.FORMAT_PASSWORD)
    private String password;
    @Size(min = 8, max = 20)
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,20})",
            message = Strings.FORMAT_PASSWORD)
    private String rePassword;
}
