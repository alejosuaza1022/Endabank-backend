package com.endava.endabank.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Strings {
    public static final String ROLE_NOT_FOUND = "The role was not found on the database";
    public static final String IDENTIFIER_TYPE_NOT_FOUND = "The identifier type was not found on the database";
    public static final String USER_NOT_FOUND = "The User was not found on the database";
    public static final String MAIL_SENT = "Mail Sent";
    public static final String MAIL_FAIL = "There was an error sending the mail";
    public static final String OLD_PASSWORD_NOT_MATCH = "The old password does not match";
    public static final String PASSWORDS_DOESNOT_MATCH = "The passwords does not match";
    public static final String TOKEN_RESET_PASSWORD_INVALID = "The token is not valid for this user";
    public static final String USER_TOKEN_NOT_FOUND = "The Token for this user was not found on the database";
    public static final String PASSWORD_UPDATED = "Password update";
    public static final String CONSTRAINT_IDENTIFIER_VIOLATED = "the user identifier provided already exist";
    public static final String CONSTRAINT_EMAIL_VIOLATED = "the user email provided already exist";
    public static final String SECRET_JWT = System.getenv("JWT_ENDABANK_SECRET");
    public static final String MESSAGE_RESPONSE = "message";
    public static final String STATUS_CODE_RESPONSE = "status code";
    public  static  final String BAD_DATA_FOR_TOKEN_GENERATION = "The id and the username are required for the token creation";
}
