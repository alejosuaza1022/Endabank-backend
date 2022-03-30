package com.endava.endabank.dto.user;

import com.endava.endabank.dto.IdentifierTypeDto;
import com.endava.endabank.dto.RoleDto;
import lombok.Data;

@Data
public class UserRegisterGetDto {
    private String email;
    private String phoneNumber;
    private String identifier;
    private String firstName;
    private String lastName;
    private boolean isApproved;
    private RoleDto role;
    private IdentifierTypeDto typeIdentifier;
}
