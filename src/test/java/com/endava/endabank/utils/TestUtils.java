package com.endava.endabank.utils;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dto.user.UpdatePasswordDto;
import com.endava.endabank.dto.user.UserDetailsDto;
import com.endava.endabank.dto.user.UserPrincipalSecurity;
import com.endava.endabank.dto.user.UserRegisterDto;
import com.endava.endabank.dto.user.UserRegisterGetDto;
import com.endava.endabank.dto.user.UserToApproveAccountDto;
import com.endava.endabank.model.ForgotUserPasswordToken;
import com.endava.endabank.model.IdentifierType;
import com.endava.endabank.model.Permission;
import com.endava.endabank.model.Role;
import com.endava.endabank.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {
    public static final String SECRET_DUMMY = "ZHVtbXkgdmFsdWUK";

    public static User getUserAdmin() {
        Role adminRole = TestUtils.adminRole();
        IdentifierType cc = TestUtils.identifierTypeCC();
        return User.builder().
                id(1).
                email("admin@endava.com").
                phoneNumber("3212312321").
                identifier("1001000000").
                firstName("Endava").
                lastName("Admin").
                isApproved(true).
                role(adminRole).
                identifierType(cc).
                bankAccounts(new ArrayList<>()).build();
    }

    public static UserToApproveAccountDto getUserApprovedAccountDto() {
        return UserToApproveAccountDto.builder().
                id(1).
                email("user@endava.com").
                isApproved(true).
                firstName("Endava").
                lastName("User").build();
    }

    public static UserToApproveAccountDto getUserNotAprrovedAccountDto() {
        UserToApproveAccountDto userToApproveAccountDto = getUserApprovedAccountDto();
        userToApproveAccountDto.setApproved(false);
        return userToApproveAccountDto;
    }

    public static User getUserNotAdmin() {
        Role userRole = TestUtils.userRole();
        IdentifierType cc = TestUtils.identifierTypeCC();
        return User.builder().
                id(1).
                email("user@endava.com").
                phoneNumber("3210000000").
                identifier("1001000000").
                firstName("Endava").
                lastName("User").
                password("$2a$10$bUcuzJbChZheTqPERIqk3u7COWhAF1CV6OU.LUkCG6iZYRhXydRqW").
                isApproved(true).
                role(userRole).
                identifierType(cc).
                bankAccounts(new ArrayList<>()).build();
    }

    public static User getUserNotAdminNonApproved() {
        User user = getUserNotAdmin();
        user.setIsApproved(false);
        return user;
    }


    public static Role adminRole() {
        return new Role(1, "ROLE_ADMIN", new ArrayList<>(), adminPermission());
    }

    public static HashSet<Permission> adminPermission() {
        Permission permission = new Permission(1, "Dummy-permission", new ArrayList<>());
        HashSet<Permission> set = new HashSet<>();
        set.add(permission);
        return set;
    }

    public static Role userRole() {
        return new Role(2, "ROLE_USER", new ArrayList<>(), new HashSet<>());
    }

    public static IdentifierType identifierTypeCC() {
        return new IdentifierType(1, "CC", new ArrayList<>());
    }

    public static UserRegisterDto getUserRegisterDto() {
        return UserRegisterDto.builder().
                email("user@endava.com").
                phoneNumber("3210000000").
                identifier("1001000000").
                firstName("Endava").
                lastName("User").
                password("Aa123456*").
                typeIdentifierId(1).build();
    }

    public static UserRegisterGetDto userRegisterGetDto() {
        return new ModelMapper().
                map(getUserNotAdmin(), UserRegisterGetDto.class);
    }
    public static UserDetailsDto userDetailsGetDto(UserPrincipalSecurity user) {
        return new ModelMapper().
                map(user, UserDetailsDto.class);
    }

    public static UserPrincipalSecurity getUserPrincipalSecurity() {
        return UserPrincipalSecurity.builder().
                id(1).
                email("user@endava.com").
                phoneNumber("3210000000").
                firstName("principal").
                isApproved(true).build();
    }

    public static UpdatePasswordDto getUpdatePasswordDto() {
        return UpdatePasswordDto.builder().
                oldPassword("A1234567!a").
                password("A1234567!aa").
                rePassword("A1234567!aa").build();
    }

    public static ForgotUserPasswordToken getForgotUserPasswordToken(String token) {
        ForgotUserPasswordToken forgotUserPasswordToken = new ForgotUserPasswordToken();
        forgotUserPasswordToken.setUser(TestUtils.getUserNotAdmin());
        forgotUserPasswordToken.setId(1);
        forgotUserPasswordToken.setToken(token);
        return forgotUserPasswordToken;
    }

    public static Map<String, Object> getSuccesfullEmailResponse() {
        Map<String, Object> map = new HashMap<>();
        map.put(Strings.MESSAGE_RESPONSE, Strings.MAIL_SENT);
        map.put(Strings.STATUS_CODE_RESPONSE, HttpStatus.valueOf(HttpStatus.ACCEPTED.value()));
        return map;
    }

    public static Map<String, Object> getFailEmailResponse() {
        Map<String, Object> map = new HashMap<>();
        map.put(Strings.MESSAGE_RESPONSE, Strings.MAIL_FAIL);
        map.put(Strings.STATUS_CODE_RESPONSE, HttpStatus.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()));
        return map;
    }

    public static UserPrincipalSecurity getUserAdminPrincipalSecurity() {

        return UserPrincipalSecurity.builder().
                id(1).
                email("admin@endava.com").
                firstName("Endava").
                phoneNumber("3210000000").
                isApproved(true).build();
    }
}
