package com.endava.endabank.dto.user;
import lombok.Data;
@Data
public class UserToApproveAccountDto {
    private String firstName;
    private String lastName;
    private String email;
    private Integer id;
    private boolean isApproved;
}
