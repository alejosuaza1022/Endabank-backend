package com.endava.endabank.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Strings {
    public static final String ROLE_NOT_FOUND = "The role was not found on the database.";
    public static final String EMAIL_NOT_MATCH = "The email was not correct.";
    public static final String ACCOUNT_NOT_FOUND = "The account was not found on the database.";
    public static final String FORMAT_PASSWORD = "1 Capital, 1 Special character, 1 Number and 8 to 20 digits,";
    public static final String IDENTIFIER_TYPE_NOT_FOUND = "The identifier type was not found on the database.";
    public static final String MERCHANT_REQUEST_STATE_NOT_FOUND = "The merchant request state was not found on the database.";
    public static final String USER_NOT_FOUND = "The user was not found on the database.";
    public static final String MAIL_SENT = "Mail Sent";
    public static final String MAIL_FAIL = "There was an error sending the mail.";
    public static final String OLD_PASSWORD_NOT_MATCH = "The old password does not match.";
    public static final String PASSWORDS_DOES_NOT_MATCH = "The passwords do not match.";
    public static final String TOKEN_RESET_PASSWORD_INVALID = "The token is not valid for this user.";
    public static final String USER_TOKEN_NOT_FOUND = "The token for this user was not found on the database.";
    public static final String PASSWORD_UPDATED = "Password updated.";
    public static final String CONSTRAINT_IDENTIFIER_VIOLATED = "There is already a user registered with this identifier.";
    public static final String CONSTRAINT_TAX_ID_VIOLATED = "There is already a merchant registered with this tax id";
    public static final String CONSTRAINT_MERCHANT_VIOLATED = "There is already a request registered for the current user";
    public static final String CONSTRAINT_EMAIL_VIOLATED = "There is already a user registered with this email.";
    public static final String SECRET_JWT = System.getenv("JWT_ENDABANK_SECRET");
    public static final String API_KEY = "Aa789rtTHGaWerss787sdf10";
    public static final String MESSAGE_RESPONSE = "message";
    public static final String STATUS_CODE_RESPONSE = "statusCode";
    public static final String BAD_DATA_FOR_TOKEN_GENERATION = "The data required for the token generation is not provided.";
    public static final String EMAIL_VERIFIED = "All set, the email was successfully verified.";
    public static final String EMAIL_NOT_VERIFIED = "The user email has not been verified yet.";
    public static final String EMAIL_FOR_VERIFICATION_SENT = "User registered, Check your email for the verification link.";
    public static final String EMAIL_ALREADY_VERIFIED = "The email was already verified.";
    public static final String EMAIL_AS_VERIFY_EMAIL = "Email Verification";
    public static final String EMAIL_AS_RESET_PASSWORD = "Reset Password";
    public static final String BAD_DATA_FOR_TOKEN_VERIFICATION = "The data required for the verification is missing.";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String ROL = "rol";
    public static final String IS_APPROVED = "isApproved";
    public static final String USER_BODY = "user";
    public static final String EMPTY_PARAMETERS = "All parameters must not be null";
    public static final String USER_ID_BODY = "userId";
    public static final String ROLE_REQUIRED = "Role is required";
    public static final String BEARER = "Bearer ";
    public static final String JWT_ERROR_TOKEN_VERIFICATION = "The token is not valid for this user.";
    public static final String AUTHORIZATION_BODY = "Authorization";
    public static final String BEARER_TEST = "Bearer token";
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String BANK_ACCOUNT_NOT_FOUND = "The bank account provided does not exist.";
    public static final String USER_NOT_OWN_BANK_ACCOUNT = "The user not owns the bank account used.";
    public static final String NOT_FOUNDS_ENOUGH = "The user has not balance enough to make the transaction.";
    public static final String TRANSACTION_COMPLETED = "The transaction was successfully completed";
    public static final Integer TRANSACTION_APPROVED_STATE = 1;
    public static final Integer TRANSACTION_FAILED_STATE = 2;
    public static final Integer TRANSACTION_PENDING_STATE = 3;
    public static final Integer TRANSACTION_AUTHORISED_STATE = 4;
    public static final Integer TRANSACTION_ERROR_STATE = 5;
    public static final Integer TRANSACTION_REFUSED_STATE = 6;
    public static final String AMOUNT_NOT_VALID = "The amount to be transferred has to be greater than 0.";
    public static final String TRANSACTION_SAME_ACCOUNT = "The account to be transferred is the same as the one used.";
    public static final String ACCOUNT_TYPE_NOT_FOUND = "The account type was not found on the database.";
    public static final String MERCHANT_NOT_FOUND = "The merchant was not found in the database";
    public static final String MERCHANT_BANK_ACCOUNT_NOT_FOUND = "The merchant's bank account was not found in the database";
    public static final String ACCOUNT_CREATED = "The bank account was successfully created.";
    public static final String MERCHANT_REQUEST_CREATED = "The request to become a merchant was successfully created.";
    public static final String MERCHANT_REQUEST_UPDATED = "The merchant request was updated";
    public static final String ACCOUNT_SUMMARY_SORT = "create_at";
    public static final String MERCHANT_REQUESTS_SORT = "create_at";
    public static final String BAD_API_ID = "The Api Id does not match merchant data";
    public static final String BAD_USER_DATA = "Data user does not match";
    public static final String STATUS_ERROR = "ERROR";
    public static final String STATUS_FRAUD = "FRAUD";
    public static final String STATUS_REFUSED = "REFUSED";
    public static final String STATUS_AUTHORISED = "AUTHORISED";

    public static final String TRANSACTION_CREATED_AND_PENDING = "The transaction has been created, is pending";

}
