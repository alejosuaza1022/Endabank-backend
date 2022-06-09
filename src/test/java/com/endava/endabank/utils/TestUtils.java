package com.endava.endabank.utils;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dto.BankAccountDto;
import com.endava.endabank.dto.BankAccountMinimalDto;
import com.endava.endabank.dto.CreateBankAccountDto;
import com.endava.endabank.dto.StateTypeDto;
import com.endava.endabank.dto.merchant.MerchantDataFilterAuditDto;
import com.endava.endabank.dto.merchant.MerchantGetFilterAuditDto;
import com.endava.endabank.dto.transaction.TransactionCreateDto;
import com.endava.endabank.dto.transaction.TransactionCreatedDto;
import com.endava.endabank.dto.user.UpdatePasswordDto;
import com.endava.endabank.dto.user.UserDetailsDto;
import com.endava.endabank.dto.user.UserPrincipalSecurity;
import com.endava.endabank.dto.user.UserRegisterDto;
import com.endava.endabank.dto.user.UserRegisterGetDto;
import com.endava.endabank.dto.user.UserToApproveAccountDto;
import com.endava.endabank.model.AccountType;
import com.endava.endabank.model.BankAccount;
import com.endava.endabank.model.ForgotUserPasswordToken;
import com.endava.endabank.model.IdentifierType;
import com.endava.endabank.model.Permission;
import com.endava.endabank.model.Role;
import com.endava.endabank.model.StateType;
import com.endava.endabank.model.Transaction;
import com.endava.endabank.model.TransactionType;
import com.endava.endabank.model.User;
import com.endava.endabank.security.UserAuthentication;
import com.sendgrid.Response;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {
    public static final String SECRET_DUMMY = "ZHVtbXkgdmFsdWUK";

    public static User getUserAdmin() {
        Role adminRole = TestUtils.adminRole();
        IdentifierType cc = TestUtils.identifierTypeCC();
        return User.builder().
                id(1).
                email("admin@test.test").
                phoneNumber("3212312321").
                identifier("1001000000").
                firstName("User").
                lastName("Admin").
                password("$2a$10$bUcuzJbChZheTqPERIqk3u7COWhAF1CV6OU.LUkCG6iZYRhXydRqW").
                isEmailVerified(true).
                isApproved(true).
                role(adminRole).
                identifierType(cc).
                bankAccounts(new ArrayList<>()).build();
    }

    public static UserToApproveAccountDto getUserApprovedAccountDto() {
        return UserToApproveAccountDto.builder().
                id(1).
                email("user@test.test").
                isApproved(true).
                firstName("User").
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
                email("user@test.test").
                phoneNumber("3210000000").
                identifier("1001000000").
                firstName("User").
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
                email("user@test.test").
                phoneNumber("3210000000").
                identifier("1001000000").
                firstName("User").
                lastName("User").
                password("$2a$10$bUcuzJbChZheTqPERIqk3u7COWhAF1CV6OU.LUkCG6iZYRhXydRqW").
                typeIdentifierId(1).build();
    }

    public static UserRegisterGetDto getUserRegisterGetDto() {
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
                email("user@test.com").
                phoneNumber("3210000000").
                firstName("principal").
                isApproved(true).build();
    }

    public static UserAuthentication getUserAuthentication() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UserAuthentication userAuth = new UserAuthentication("user@test.test", "test-password", authorities);
        userAuth.setId(1);
        userAuth.setIsApproved(true);
        userAuth.setIsEmailVerified(true);
        return userAuth;
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
                email("admin@test.test").
                firstName("User").
                phoneNumber("3210000000").
                isApproved(true).build();
    }

    public static List<User> getUserList() {
        return Arrays.asList(getUserNotAdmin(), getUserAdmin());
    }

    public static Response getSendGridResponse() {
        Response response = new Response();
        response.setStatusCode(202);
        response.setBody("Success");
        response.setHeaders(new HashMap<>());
        return response;
    }

    public static UserToApproveAccountDto getUserNotApprovedAccountDto() {
        UserToApproveAccountDto userToApproveAccountDto = getUserApprovedAccountDto();
        userToApproveAccountDto.setApproved(false);
        return userToApproveAccountDto;
    }

    public static AccountType getAccountType() {
        return new AccountType(1, "DEBIT", new ArrayList<>());
    }

    public static CreateBankAccountDto getCreateBankAccountDto() {
        AccountType accountType = TestUtils.getAccountType();
        User user = TestUtils.getUserAdmin();
        return CreateBankAccountDto.builder().
                id(1).
                accountNumber("1111111111111111").
                accountType(accountType).
                balance(1000000.0).
                password("1111").
                user(user).build();
    }


    public static BankAccount getBadBankAccount() {
        AccountType accountType = TestUtils.getAccountType();
        User user = TestUtils.getUserAdmin();
        return BankAccount.builder().
                id(1).
                accountNumber(BigInteger.valueOf(Long.parseLong("10000000000001"))).
                accountType(accountType).
                balance(1000000.0).
                password("$2a$10$caewIC6lyX2A3c0qF1UMFeF8zyVwSZGiMUrPWst/0Cy.B/Xxnmh/u"). // 1111 encode
                        user(user).build();
    }

    public static TransactionCreateDto getTransactionCreateDto() {
        return TransactionCreateDto.builder().
                amount(10000.0).
                description("description").
                bankAccountNumberIssuer(BigInteger.valueOf(Long.parseLong("1111111111111111"))).
                bankAccountNumberReceiver(BigInteger.valueOf(Long.parseLong("0000000000000001"))).
                address("235.30.171.21").build();
    }

    public static Transaction getTransaction() {
        TransactionType transactionType = TransactionType.builder().id(1).build();
        BankAccount bankAccountIssuer = getBankAccount();
        BankAccount bankAccountReceiver = getBankAccount2();
        return Transaction.
                builder().amount(10000.0).
                description("description").
                address("235.30.171.21").
                transactionType(transactionType).bankAccountIssuer(bankAccountIssuer)
                .stateDescription(Strings.TRANSACTION_COMPLETED).
                bankAccountReceiver(bankAccountReceiver).build();
    }
    public static List<MerchantDataFilterAuditDto> getMerchantList() {
        return List.of(MerchantDataFilterAuditDto.builder().storeName("testStore")
                .reviewedByFirstName("admin")
                .updatedAt("2020-09-06")
                .merchantRequestStateName("APPROVED")
                .build());
    }
    public static MerchantGetFilterAuditDto getMerchant() {
        return MerchantGetFilterAuditDto.builder()
                .totalElements(1)
                .totalPages(1)
                .size(10)
                .content(getMerchantList())
                .build();
    }

    public static TransactionCreatedDto getTransactionCreatedDto() {
        StateTypeDto stateTypeDto = StateTypeDto.builder().id(1).name("APPROVED").build();
        return TransactionCreatedDto.builder().
                id(1).
                amount(1000.0).bankAccountIssuer(getBankAccountDto()).
                bankAccountReceiver(getBankAccountMinimalDto()).
                stateType(stateTypeDto).stateDescription(Strings.TRANSACTION_COMPLETED).build();
    }

    public static StateType getStateTypeApproved() {
        return StateType.builder().id(1).name("APPROVED").build();
    }

    public static StateType getStateTypeFailed() {
        return StateType.builder().id(1).name("FAILED").build();
    }

    public static BankAccountDto getBankAccountDto() {
        return BankAccountDto.builder().
                id(1).
                accountNumber(BigInteger.valueOf(Long.parseLong("1111111111111111"))).
                balance(1000000.0).build();
    }

    public static BankAccountMinimalDto getBankAccountMinimalDto() {
        return BankAccountMinimalDto.builder().
                id(1).
                accountNumber(BigInteger.valueOf(Long.parseLong("0000000000000001"))).build();
    }

    public static BankAccount getBankAccount() {
        AccountType accountType = TestUtils.getAccountType();
        User user = TestUtils.getUserAdmin();
        return BankAccount.builder().
                id(1).
                accountNumber(BigInteger.valueOf(Long.parseLong("1111111111111111"))).
                accountType(accountType).
                balance(1000000.0).
                password("$2a$10$caewIC6lyX2A3c0qF1UMFeF8zyVwSZGiMUrPWst/0Cy.B/Xxnmh/u"). // 1111 encode
                        user(user).build();
    }

    public static BankAccount getBankAccount2() {
        BankAccount bankAccount = getBankAccount();
        bankAccount.setAccountNumber(BigInteger.valueOf(Long.parseLong("0000000000000001")));
        return bankAccount;
    }
}
