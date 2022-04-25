package com.endava.endabank.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationDto {
    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

    public void setEmail(String email){
        this.email = email.toLowerCase();
    }
}
