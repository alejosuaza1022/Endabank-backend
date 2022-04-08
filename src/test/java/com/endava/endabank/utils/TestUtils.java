package com.endava.endabank.utils;

import com.endava.endabank.model.IdentifierType;
import com.endava.endabank.model.Role;
import com.endava.endabank.model.User;

import java.util.ArrayList;
import java.util.HashSet;

public class TestUtils {
    public static User getUserAdmin() {
        Role adminRole = TestUtils.adminRole();
        IdentifierType cc = TestUtils.identifierType();
        return User.builder().
                id(1).
                email("admin@endava.com").
                phoneNumber("3212312321").
                identifier("1001000000").
                firstName("Endava ").
                lastName("user").
                isApproved(true).
                role(adminRole).
                identifierType(identifierType()).
                bankAccounts(new ArrayList<>()).build();
    }

    public static Role adminRole() {
        return new Role(1, "ADMIN", new ArrayList<>(), new HashSet<>());
    }

    public static IdentifierType identifierType() {
        return new IdentifierType(1, "CC", new ArrayList<>());
    }
}
