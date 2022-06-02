package com.endava.endabank.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipalSecurity {
    private Integer id;
    private String email;
    private String identifier;
    private String identifierName;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private boolean isApproved;
}
