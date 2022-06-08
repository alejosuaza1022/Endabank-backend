package com.endava.endabank.dto.user;

import lombok.Data;

@Data
public class UserGeneralInfoDto {
    private String email;
    private String identifier;
    private String identifierName;
    private String firstName;
    private String phone;
    private String lastName;
}
