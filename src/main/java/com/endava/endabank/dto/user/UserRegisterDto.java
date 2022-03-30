package com.endava.endabank.dto.user;


import lombok.Data;

@Data
public class UserRegisterDto {
    private Integer id;
    private String email;
    private String phoneNumber;
    private String identifier;
    private String firstName;
    private String lastName;
    private String password;
    private Integer roleId;
    private Integer typeIdentifierId;
}
